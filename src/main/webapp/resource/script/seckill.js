// javaScript模块化
var seckill = {
    //  封装秒杀相关ajax的url
    URL : {
        now: function () {
            return '/api/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/api/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/api/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    //  详情页秒杀逻辑
    detail: {
        init : function (params) {
            //  用户手机验证和登陆，计时交互
            //  在cookie中查找手机号
            var killPhone = $.cookie('killPhone');

            if (!seckill.validatePhone(killPhone)) {
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',    // 禁止位置关闭
                    keyboard: false        //禁止键盘时间
                });
                $('#killPhoneBtn').click(function(){
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        //  cookie有效期7天，路径在/api/seckill有效
                        $.cookie('killPhone', inputPhone, {expires:7, path:'/api/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html(
                            // 此处设置为前端字典更为合适
                            '<label class="label label-danger">手机号错误！</label>'
                        ).show(300);
                    }
                });
            }
            //  已登陆
            //  计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            //  ajax简写
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['result'];
                    //  时间判断，计时交互
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
                    // TODO
                    console.log('result :' + result);
                }
            });
        }
    },

    //  验证手机号
    validatePhone : function(phone){
        if (phone && phone.length == 11 && !isNaN(phone)){
            return true;
        } else {
            return false;
        }
    },
    //  计时交互
    countDown: function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        if (nowTime > endTime) {
            seckillBox.html('秒杀已结束！');
        } else if (nowTime < startTime) {
            //  秒杀尚未开启,计时事件绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                //  时间的格式
                var formate = event.strftime('秒杀计时： %D天 %H时 %M分 %S秒');
                seckillBox.html(formate);
                //  时间完成后回调事件
            }).on('finish.countdown', function () {
                seckill.handleSeckill(seckillId, seckillBox);
            });
        } else {
            //  秒杀开始
            seckill.handleSeckill(seckillId, seckillBox);
        }
    },
    //  处理秒杀逻辑
    handleSeckill: function (seckillId, node) {
        //  获取秒杀地址，控制显示逻辑,并执行秒杀
        node.hide().html(
            '<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.ajax({
            type: 'POST',
            url: seckill.URL.exposer(seckillId),
            data: {},
            dataType: 'json',
            success: function (data) {
        // $.post(seckill.URL.exposer(seckillId), {}, function (data) {
                if (data && data['success']) {
                    var exposer = data['result'];
                    if (exposer['exposed']) {
                        //  开启秒杀
                        var md5 = exposer['md5'];
                        var killURL = seckill.URL.execution(seckillId, md5);
                        //  TODO
                        console.log("killURL : " + killURL);
                        //  绑定一次点击事件
                        $('#killBtn').one('click', function () {
                            //  1.禁用按钮
                            $(this).addClass('disabled');
                            //  2.发送秒杀请求,执行秒杀
                            $.ajax({
                                type: 'POST',
                                data: {},
                                dataType: 'json',
                                url: killURL,
                                success: function (data) {
                            // $.post(killURL, {}, function (data) {
                                    if (data && data['success']) {
                                        var killResult = data['result'];
                                        var state = killResult['state'];
                                        var stateInfo = killResult['info'];
                                        //  3.显示秒杀结果
                                        node.html(
                                            '<span class="label label-success">' + stateInfo + '</span>'
                                        );
                                    }
                                }
                            });
                        });
                        node.show();
                    } else {
                        //  未开启秒杀
                        var now = exposer['now'];
                        var start = exposer['start'];
                        var end = exposer['end'];
                        seckill.countDown(seckillId, now, start, end);
                    }
                } else {
                    //  TODO
                    console.log("result : " + data);
                }
            }
        });
    }
}