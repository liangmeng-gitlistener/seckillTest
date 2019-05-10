// javaScript模块化
var seckill = {
    //  封装秒杀相关ajax的url
    URL : {
        now: function () {
            return '/api/seckill/time/now';
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
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];
                    //  时间判断，计时交互
                    seckill.countDown(seckillId, nowTime, startTime, endTime);
                } else {
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
            });
        } else {

        }
    }
}