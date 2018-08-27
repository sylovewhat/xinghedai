<?php
namespace app\index\controller;
use app\index\model\User;
use app\index\validate\UserValidate;
use \think\Request;
use \think\Db;

//用户注册登入控制器
class Admin 
{

    

    //-------------------------------------------用户登入--------------------------------------------------------
    public function login()
    {

       //获取传入值
       $data=input('get.');
       //获取验证其实例
       $mUserValidate=new UserValidate();
       //表单验证
       if(!$mUserValidate->check($data))
       {
           //如果表单规则验证失败
           $returndata = array(
                'code' => '205',
                'data'=> $mUserValidate->getError(),
                'status'=>"error",
                'message'=>'验证失败',
            ); 
            return json($returndata);
       }else
       {
         //实例化用户
         $muser=new User;
         //查询用户
         $result=$muser->where('phone', $data['phone'])->where('password',$data['password'])->find();
         
         if($result===null){
            $returndata = array(
                'code' => '400',
                'data'=> "",
                'status'=>"error",
                'message'=>'登入失败',
            ); 
            
            return json($returndata);
         }else
         {
            
            $sumComm=$this->getuserxgbnum($result['Id']);//计算出对应用户所有讯狗币数量
            
            if($sumComm[0]['sumComm']==null)
            {
              
              $sumComm[0]['sumComm']=0;
              
            }
            $returndata = array(
                'code' => '200',
                'sumComm'=>$sumComm[0]['sumComm'],
                'data'=>  $result,
                'status'=>"success",
                'message'=>'登入成功',
            ); 
           
            return json($returndata);
         }
         
       }
    }

//-------------------------------------------用户注册--------------------------------------------------------


    //注册用户
    public function register()
    {
       //获取当前请求信息
       $request = request();
       
       //获取传入值
       $data=input('get.');

       //获取验证其实例
       $mUserValidate=new UserValidate();
       //表单验证  
       if(!$mUserValidate->check($data))
       {
           //如果表单规则验证失败
           $returndata = array(
                'code' => '205',
                'data'=> $mUserValidate->getError(),
                'status'=>"error",
                'message'=>'验证失败',
            ); 
            return json($returndata);
                  
       }else
       {

         $muser = User::create([
             'phone' => $data['phone'],
             'password' => $data['password']
         ]);

         if($muser===false){
            $returndata = array(
                'code' => '400',
                'data'=> "",
                'status'=>"error",
                'message'=>'注册失败',
            ); 
            
            return json($returndata);
         }else
         {
            //根据插入的ID进行查询数据
            $muser=User::get($muser->Id);

            $returndata = array(
                'code' => '200',
                'data'=>  $muser,
                'status'=>"success",
                'message'=>'注册成功',
            ); 

            return json($returndata);
         }
       }
    }



  //登入成功后根据返回回来的用户ID查询改用户总共有的讯狗币数量
   function getuserxgbnum($userid)
  {

       
        $result = Db::query('select cast(sum(value) AS DECIMAL (19, 4)) AS sumComm from xg_xgb where userid='.$userid);
        return $result;
  }

  //忘记密码更新密码
  function forgetpassword()
  {
         //获取传入值
       $data=input('get.');
       $result = Db::table('xg_user')
                   ->where('phone', $data['phone'])
                   ->update(['password' => $data['password']]);
       if($result==0)
       {
            $returndata = array(
                'code' => '400',
                'data'=> "",
                'status'=>"error",
                'message'=>'更新密码失败',
            ); 
            
            return json($returndata);
       }else
       {
            $returndata = array(
                'code' => '200',
                'data'=>  $result,
                'status'=>"success",
                'message'=>'密码更新成功',
            ); 
           
            return json($returndata);
       }





  }


}
