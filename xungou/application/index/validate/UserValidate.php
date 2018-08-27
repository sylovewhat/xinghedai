<?php
namespace app\index\validate;
use think\Validate;
//用户登入验证
class UserValidate extends Validate
{
    // 验证规则[字段名，规则，验证信息]
    protected $rule = [
        ['phone','require','用户名不能为空'],
        ['password','require','密码不能为空'],
    ];
}