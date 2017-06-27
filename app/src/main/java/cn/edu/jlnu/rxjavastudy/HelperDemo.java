package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.internal.util.ActionSubscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/27.
 */

public class HelperDemo {

    private static String TAG=HelperDemo.class.getSimpleName();
    public static void  test1(){

        //一般情况下 被观察者与观察者进行订阅之后就会马上发送数据 如果使用了delay函数，可以在某段时间之后再发送
        //delaySubscription  延迟指定时间才订阅
        //delay 延迟指定时间才发送
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
            }
        })
               // .delay(3000, TimeUnit.MILLISECONDS)
                .delaySubscription(4000,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"call :"+integer);
                    }
                });
    }

    public static void  test2(){
        //doOnEach 就是监听观察者的某些方法什么时候被调用的 该函数实际上就是一个监听器
        Observable.just(1,2,3,4)
//                .doOnEach(new Observer<Integer>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.e(TAG,"doOnEach onCompleted :");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.e(TAG,"doOnEach onError :"+e.getLocalizedMessage());
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.e(TAG,"doOnEach onNext :"+integer);
//                    }
//                })

                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"doOnNext call :"+integer);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"call :"+integer);
                    }
                });
    }

    /***
     * 先检测到订阅
     * 然后再延迟订阅
     */
    public static void test3(){
        Observable.just(1,2,3)
                .delaySubscription(5000,TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e(TAG,"doOnSubscribe call :");
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"call :"+integer);
            }
        });
    }

    public static void test4(){
        //默认情况下 当被观察者调用了onCompletet()之后 此刻系统会将被观察者与观察者之间取消订阅
        //此刻我们模拟的后面的onNext()时间不会被调用
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
                subscriber.onNext(4);
                subscriber.onNext(5);
                subscriber.onCompleted();
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                Log.e(TAG,"取消订阅");
            }
        })
                .serialize() //安全操作，为了防止不安全的订阅造成的问题

                .unsafeSubscribe(new Subscriber<Integer>() { //不安全订阅
            //使用unsafeSubscribe 可以让观察者与被观察者之间无取消订阅的操作
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG,"call :"+integer);
            }
        });
    }

    public static void test5(){
        //一般该函数用在网络超时处理中
        Observable.interval(2000,TimeUnit.MILLISECONDS)
                .timeout(2000,TimeUnit.MILLISECONDS) //大于等于2秒没发送数据就报Timeout()异常
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e(TAG, "call :" + aLong);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "throwable :" + throwable);
                    }
                });
    }

    public static void test6(){
        //using函数是用来创建一个一次性资源的函数 在订阅的流程结束后
        //第一个用于创建一次性资源的工厂函数
        //第二个用于创建Observable的工厂函数
        //第三个用于释放资源的函数
        Observable<String> observable=Observable.using(new Func0<String>() {

            @Override
            public String call() { //一次性资源
                return "hello android ";
            }
        }, new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return Observable.just(s);
            }
        }, new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"清理一次性资源："+s);
                s=null;
            }
        });
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"获取数据："+s);
            }
        });
    }
}
