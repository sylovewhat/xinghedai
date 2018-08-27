/*!
 * ======================================================
 * FeedBack Template For MUI (http://dev.dcloud.net.cn/mui)
 * =======================================================
 * @version:1.0.0
 * @author:cuihongbao@dcloud.io
 */
(function() {
	var index = 1;
	var size = null;
	var imageIndexIdNum = 0;
	var starIndex = 0;
	var feedback = {
		question: document.getElementById('question'),
		contact: document.getElementById('contact'),
		imageList: document.getElementById('image-list'),
		submitBtn: document.getElementById('submit'),
		votes: ''
	};
	var feedbackUrl = 'https://service.dcloud.net.cn/feedback';
	var uploadImageUrl = 'https://service.dcloud.net.cn/feedback/image';
	feedback.files = [];
	feedback.uploader = null;
	feedback.deviceInfo = null;
	mui.plusReady(function() {
		//设备信息，无需修改
		feedback.deviceInfo = {
			appid: plus.runtime.appid,
			imei: plus.device.imei, //设备标识
			images: feedback.files, //图片文件
			p: mui.os.android ? 'a' : 'i', //平台类型，i表示iOS平台，a表示Android平台。
			md: plus.device.model, //设备型号
			app_version: plus.runtime.version,
			plus_version: plus.runtime.innerVersion, //基座版本号
			os: mui.os.version,
			net: '' + plus.networkinfo.getCurrentType(),
			vendor: plus.device.vendor,
			channel: plus.runtime.channel ? plus.runtime.channel : ''
		}
	});
	/**
	 *提交成功之后，恢复表单项 
	 */
	feedback.clearForm = function() {
		feedback.question.value = '';
		feedback.contact.value = '';
		feedback.imageList.innerHTML = '';
		feedback.newPlaceholder();
		feedback.files = [];
		feedback.votes = '';
		index = 0;
		size = 0;
		imageIndexIdNum = 0;
		starIndex = 0;
		//清除所有星标
		mui('.icons i').each(function(index, element) {
			if(element.classList.contains('mui-icon-star-filled')) {
				element.classList.add('mui-icon-star')
				element.classList.remove('mui-icon-star-filled')
			}
		})
	};
	feedback.getFileInputArray = function() {
		return [].slice.call(feedback.imageList.querySelectorAll('.file'));
	};
	feedback.addFile = function(path) {
		feedback.files.push({
			name: "images" + index,
			path: path
		});
		index++;
	};
	/**
	 * 初始化图片域占位
	 */
	feedback.newPlaceholder = function() {
		var fileInputArray = feedback.getFileInputArray();
		if(fileInputArray &&
			fileInputArray.length > 0 &&
			fileInputArray[fileInputArray.length - 1].parentNode.classList.contains('space')) {
			return;
		};
		imageIndexIdNum++;
		var placeholder = document.createElement('div');
		placeholder.setAttribute('class', 'image-item space');
		var up = document.createElement("div");
		up.setAttribute('class', 'image-up')
			//删除图片
		var closeButton = document.createElement('div');
		closeButton.setAttribute('class', 'image-close');
		closeButton.innerHTML = 'X';
		//小X的点击事件
		closeButton.addEventListener('tap', function(event) {
			setTimeout(function() {
				feedback.imageList.removeChild(placeholder);
			}, 0);
			return false;
		}, false);

		//
		var fileInput = document.createElement('div');
		fileInput.setAttribute('class', 'file');
		fileInput.setAttribute('id', 'image-' + imageIndexIdNum);
		fileInput.addEventListener('tap', function(event) {
			var self = this;
			var index = (this.id).substr(-1);

			plus.gallery.pick(function(e) {
				//				console.log("event:"+e);
				var name = e.substr(e.lastIndexOf('/') + 1);
				console.log("name:" + name);

				// TODO 压缩图片

				feedback.uploadImage(e, fileInput, placeholder, up);
			}, function(e) {
				mui.toast(e.message);
			}, {});
		}, false);
		placeholder.appendChild(closeButton);
		placeholder.appendChild(up);
		placeholder.appendChild(fileInput);
		feedback.imageList.appendChild(placeholder);
	};
	feedback.newPlaceholder();
	feedback.submitBtn.addEventListener('tap', function(event) {
		if(feedback.question.value == '' ||
			(feedback.contact.value != '' &&
				feedback.contact.value.search(/^(\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+)|([1-9]\d{4,9})$/) != 0)) {
			return mui.toast('信息填写不符合规范');
		}

		var shortcuts = [
			'桌面快捷方式创建失败', '桌面快捷方式没有', '桌面无图标', '创建快捷方式失败', '桌面图标显示不了',
			'桌面没图标', '无法显示图标', '显示不了图标'
		];
		var len = shortcuts.length;
		for (var i=0; i<len; i++) {
			if (feedback.question.value.match(shortcuts[i])) {
				if (openHint()) {
					return;
				}
			}
		}

		var tasks = [
			'任务管理器里看不到进程', '找不到进程', '无进程', '进程里看不到', '进程里找不到', '任务里找不到', '任务里看不到'
		];
		len = tasks.length;
		for (var i=0; i<len; i++) {
			if (feedback.question.value.match(tasks[i])) {
				repairTask();
				return;
			}
		}

		feedback.votes = getVoteResult();
		// 判断 vote 内容
		if (/不好，|不好。|不好,|不好，|不好！|不好!|垃圾|不好用|不喜欢|^不好$|^差$/.test(feedback.question.value.trim()) || starIndex == 1 || starIndex == 2) {
			if (feedback.votes == '') {
				jumpToVote();
				return mui.toast('请选择您不满意的地方');
			}
		}

		if(feedback.question.value.length > 200 || feedback.contact.value.length > 200) {
			return mui.toast('信息超长,请重新填写~')
		}
		//判断网络连接
		if(plus.networkinfo.getCurrentType() == plus.networkinfo.CONNECTION_NONE) {
			return mui.toast("连接网络失败，请稍后再试");
		}
		feedback.send(mui.extend({}, feedback.deviceInfo, {
			content: feedback.question.value,
			contact: feedback.contact.value,
			images: feedback.files,
			score: '' + starIndex,
			asyncImage: 'true',
			votes: feedback.votes
		}))
	}, false)
	feedback.send = function(content) {
		feedback.uploader = plus.uploader.createUpload(feedbackUrl, {
			method: 'POST'
		}, function(upload, status) {
			//			plus.nativeUI.closeWaiting()
			console.log("upload cb:" + upload.responseText);
			if(status == 200) {
				var data = JSON.parse(upload.responseText);
				//上传成功，重置表单
				if(data.ret === 0 && data.desc === 'Success') {
					//					mui.toast('反馈成功~')
					console.log("upload success");

				}
			} else {
				console.log("upload fail");
			}

		});
		//添加上传数据
		mui.each(content, function(index, element) {
			if(index !== 'images') {
				console.log("addData:" + index + "," + element);
				feedback.uploader.addData(index, element)
			} else {
				mui.each(feedback.files, function(index, element) {
					var f = feedback.files[index];
					console.log("add image:" + f.name + ',' + f.path);
					feedback.uploader.addData(f.name, f.path)
				});
			}
		});
		
		//开始上传任务
		feedback.uploader.start();
		mui.alert("感谢反馈，点击确定关闭", "问题反馈", "确定", function() {
			feedback.clearForm();
			mui.back();
		});
		//		plus.nativeUI.showWaiting();
	};

	feedback.uploadImage = function(file, fileInput, placeholder, up) {
		feedback.uploader = plus.uploader.createUpload(uploadImageUrl, {
			method: 'POST'
		}, function(upload, status) {
			//			plus.nativeUI.closeWaiting()
			console.log("upload cb:" + upload.responseText);
			plus.nativeUI.closeWaiting();
			if(status == 200) {
				var data = JSON.parse(upload.responseText);
				//上传成功，重置表单
				if(data.ret === 0 && data.desc === 'Success') {
					//					mui.toast('反馈成功~')
					console.log("upload success");
					size += data.size
					console.log("filesize:" + data.size + ",totalsize:" + size);
					if(!fileInput.parentNode.classList.contains('space')) { //已有图片
						feedback.files.splice(index - 1, 1, {
							name: "images" + index,
							path: data.file_name
						});
					} else { //加号
						placeholder.classList.remove('space');
						feedback.addFile(data.file_name);
						feedback.newPlaceholder();
					}
					up.classList.remove('image-up');
					placeholder.style.backgroundImage = 'url(' + data.url + ')';
				}
			} else {
				console.log("upload fail");
			}

		});

		feedback.uploader.addData('appid', feedback.deviceInfo.appid);

		feedback.uploader.addFile(file, {
			key: 'image'
		});

		plus.nativeUI.showWaiting();
		//开始上传任务
		feedback.uploader.start();
	}

	//应用评分
	mui('.icons').on('tap', 'i', function() {
		var index = parseInt(this.getAttribute("data-index"));
		var parent = this.parentNode;
		var children = parent.children;
		if(this.classList.contains("mui-icon-star")) {
			for(var i = 0; i < index; i++) {
				children[i].classList.remove('mui-icon-star');
				children[i].classList.add('mui-icon-star-filled');
			}
		} else {
			for(var i = index; i < 5; i++) {
				children[i].classList.add('mui-icon-star')
				children[i].classList.remove('mui-icon-star-filled')
			}
		}
		starIndex = index;
	});
	//选择快捷输入
	mui('.mui-popover').on('tap', 'li', function(e) {
		var question = document.getElementById("question").value;
		var selection = this.children[0].innerHTML;
		if (selection == '桌面无图标') {
			mui('.mui-popover').popover('hide');
			if (!openHint()) {
				document.getElementById("question").value = question ? question + '; ' + selection : selection;
			}
			return;
		} else if (selection == '任务管理器里看不到进程') {
			mui('.mui-popover').popover('hide');
			repairTask();
			return;
		}

		document.getElementById("question").value = question ? question + '; ' + selection : selection;
		mui('.mui-popover').popover('toggle')
	})
})();