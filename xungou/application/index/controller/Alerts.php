<?php
namespace app\index\controller;
use app\index\model\Alerts as Alertsobj;
use \think\Request;
use \think\Db;
//快讯操作
class Alerts
{
    public function selectalters()
    {
        
        //获取传入值
        $data=input('get.');
         //实例化快讯对象
         $mAlerts=new Alertsobj;
         //查询快讯
         $result=Db::table('xg_alerts')->page($data['page'],$data['limit'])->order('time', 'desc')->select();
         if($result===null){
            $returndata = array(
                'code' => '400',
                'data'=> "",
                'status'=>"error",
                'message'=>'查询数据失败',
            ); 
            
            return json($returndata);
         }else
         {
            $returndata = array(
                'code' => '200',
                'data'=>  $result,
                'status'=>"success",
                'message'=>'查询数据成功',
            ); 

            return json($returndata);
         }
    }

}