<?php
namespace app\index\controller;
use app\index\model\Xgb as Agbobj;
use \think\Request;
use \think\Db;
//讯狗币
class Agb
{

    //查询讯狗币
    public function queryagb()
    {
       //获取传入值
       $data=input('get.');
       $apply_time=time();//申请讯狗币的时间
       $apply_user_id=$data['userid'];//申请讯狗币用户ID
       $mAgbobj=new Agbobj;//获取讯狗币的实例


            //-------------------------------------------------------------------------------
            //
            $result=Db::table('xg_xgb')->where('userid',$apply_user_id)->select();

            if($result==null){
               //如果查询没有用户申请记录的话，那么在进行判断是否给让他挖到币 
               $num=rand(1,100);
               
               if($num>90)//90
               {
                    $xgbnum=rand(1,5);//按随机数俩插入讯狗币数据
                    if($xgbnum==1)
                    {
                        $value1=rand(1,100)/1000;
                        $data = ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1];
                        $result=Db::table('xg_xgb')->insert($data);
                        //1代表插入成功了
                        if($result==1)
                        {
                        $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgb',
                                    ); 
                               return json($returndata);
                        }
                    }else if($xgbnum==2)
                    {
                        
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2]
                        ];
                       $result= Db::table('xg_xgb')->insertAll($data);

                       //1代表插入成功了
                        if($result==2)
                        {
                        $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgb',
                                    ); 
                              return json($returndata);
                         }
                    }else if($xgbnum==3)
                    {
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $value3=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value3]
                        ];
                        $result=Db::table('xg_xgb')->insertAll($data);
                        //1代表插入成功了
                        if($result==3)
                        {
                        $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成1xgb',
                                    ); 
                              return json($returndata);
                        }
                    }else if($xgbnum==4)
                    {
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $value3=rand(1,100)/1000;
                        $value4=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value3],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value4]
                        ];
                        $result=Db::table('xg_xgb')->insertAll($data);
                        //1代表插入成功了
                        if($result==4)
                        {
                        $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgb',
                                    ); 
                              return json($returndata);
                     }
                    }else if($xgbnum==5)
                    {
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $value3=rand(1,100)/1000;
                        $value4=rand(1,100)/1000;
                        $value5=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value3],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value4],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value5]
                        ];
                        $result=Db::table('xg_xgb')->insertAll($data);
                        //1代表插入成功了
                        if($result==5)
                        {


                                  $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }

                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgb',
                                    ); 
                              return json($returndata);
                        }
                    }

               }else
               {
                  $returndata = array(
                    'code' => '200',
                    'data'=>  "",
                    'status'=>"success",
                    'message'=>'挖币失败',
                  ); 
                  return json($returndata);

               }


             
            }
            else
            {
               //如果查询有用户申请记录的话，拿出最新的那条记录的时间和现在的时间进行，---------------------------
               
               //查询有多少条xgb没有被领取
               $result=Db::table('xg_xgb')
                       ->where('userid',$apply_user_id)
                       ->where('createtime','> time',$apply_time-86400)
                       ->where('receive',0)
                       ->select();
               if($result==null)
               {
               //这最好在来一套逻辑，来限制无限刷

               $num=rand(1,1000);
               if($num>800)
               {
                    $xgbnum=rand(1,5);//按随机数俩插入讯狗币数据
                    if($xgbnum==1)
                    {
                        $value1=rand(1,100)/1000;
                        $data = ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1];
                        $result=Db::table('xg_xgb')->insert($data);
                        //1代表插入成功了
                        if($result==1)
                        {

                                 $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }

                               
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgg失败',
                                    ); 
                               return json($returndata);
                        }
                    }else if($xgbnum==2)
                    {
                        
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2]
                        ];
                       $result= Db::table('xg_xgb')->insertAll($data);

                       //1代表插入成功了
                        if($result==2)
                        {
                                 $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgb',
                                    ); 
                              return json($returndata);
                         }
                    }else if($xgbnum==3)
                    {
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $value3=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value3]
                        ];
                        $result=Db::table('xg_xgb')->insertAll($data);
                        //1代表插入成功了
                        if($result==3)
                        {
                                                                $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'--------------------生成1xgb',
                                    ); 
                              return json($returndata);
                        }
                    }else if($xgbnum==4)
                    {
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $value3=rand(1,100)/1000;
                        $value4=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value3],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value4]
                        ];
                        $result=Db::table('xg_xgb')->insertAll($data);
                        //1代表插入成功了
                        if($result==4)
                        {
                                                                $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgb',
                                    ); 
                              return json($returndata);
                     }
                    }else if($xgbnum==5)
                    {
                        $value1=rand(1,100)/1000;
                        $value2=rand(1,100)/1000;
                        $value3=rand(1,100)/1000;
                        $value4=rand(1,100)/1000;
                        $value5=rand(1,100)/1000;
                        $data = [
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value1],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value2],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value3],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value4],
                        ['receive' => 0, 'userid' => $apply_user_id,'createtime'=>$apply_time,'value'=>$value5]
                        ];
                        $result=Db::table('xg_xgb')->insertAll($data);
                        //1代表插入成功了
                        if($result==5)
                        {
                                                                $chaxunresult=Db::table('xg_xgb')
                                           ->where('userid',$apply_user_id)
                                           ->where('createtime','> time',$apply_time-86400)
                                           ->where('receive',0)
                                           ->select();
                                  if($chaxunresult==null)
                                  {
                                        return;
                                  }else
                                  {
                                      $returndata = array(
                                      'code' => '200',
                                      'data'=>  $chaxunresult,
                                      'status'=>"success",
                                      'message'=>'生成1个xgb',
                                     ); 
                                     return json($returndata);
                                  }
                        }else
                        {
                              $returndata = array(
                                     'code' => '400',
                                     'data'=>  "",
                                     'status'=>"success",
                                     'message'=>'生成xgb',
                                    ); 
                              return json($returndata);
                        }
                    }

               }else
               {
                  $returndata = array(
                    'code' => '200',
                    'data'=>  "",
                    'status'=>"success",
                    'message'=>'挖币失败',
                  ); 
                  return json($returndata);

               }
                  
               }else
               {
                  $returndata = array(
                    'code' => '200',
                    'data'=>  $result,
                    'status'=>"success",
                    'message'=>'剩余的xgb',
                  ); 
                  return json($returndata);
               }
            }
       
       

    }



    //---------------------------------------------------------------------------------------------------------------
    //获取讯狗币
    public function getagb()
    {
        //获取传入值
       $data=input('get.');
       $userid=$data['userid'];
       $xgbid=$data['Id'];
       $mAgbobj=new Agbobj;//获取讯狗币的实例
       $result=Db::table('xg_xgb')
               ->where('Id', $xgbid)
               ->where('userid', $userid)
               ->update(['receive' => 1]);
       if($result!=0)
        {
                $returndata = array(
                'code' => '200',
                'data'=>  $result,
                'status'=>"success",
                'message'=>'领取xgb成功',
                ); 
             return json($returndata);
        }else
        {
               $returndata = array(
                'code' => '400',
                'data'=>  "",
                'status'=>"fail",
                'message'=>'领取xgb失败',
                ); 
            return json($returndata);
        }
    }


  //---------------------------------------------------------------------------------------------------------------
  //查询获取xgb的历史记录
  public function gethistory()
  {
        //获取传入值
        $data=input('get.');
         //查询讯狗币获取历史记录
         $result=Db::table('xg_xgb')->where('userid',$data['userid'])->where('receive',$data['receive'])->page($data['page'],$data['limit'])->select();
         if($result==null){
            $returndata = array(
                'code' => '200',
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