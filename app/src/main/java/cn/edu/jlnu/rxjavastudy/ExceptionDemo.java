package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ExceptionDemo {
    private static final String TAG =ExceptionDemo.class.getSimpleName() ;

    public static void test1(){
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                //有可能是你的代码出现了空指针的异常 在这里我们也可以模拟异常
                subscriber.onError(new NullPointerException("空指针异常"));
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        })
//                .subscribe(new Observer<Integer>() {
//            @Override
//            public void onCompleted() {
//                Log.e(TAG,"onCompleted()");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.e(TAG,"onError()"+e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.e(TAG,"onNext():"+integer);
//            }
//        });
                //onErrorReturn 它是用来捕获异常并且返回一个值
                .onErrorReturn(new Func1<Throwable, Integer>() {
                    @Override
                    public Integer call(Throwable throwable) {
                        Log.e(TAG,"Throwable():"+throwable.getLocalizedMessage());
                        return 666;
                    }
                })
        .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"onNext():"+integer);
            }
        });
    }

    public static void test2(){
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                //有可能是你的代码出现了空指针的异常 在这里我们也可以模拟异常
                subscriber.onError(new NullPointerException("空指针异常"));
                //只要抛出了异常 onErrorResume就会被调用
                //该方法返回的被观察者就会重新订阅到下面的Action1打印出对应的数据
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        })
            .onErrorResumeNext(Observable.just(666))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"onNext():"+integer);
                    }
                });
    }

    public static void test3(){
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                //有可能是你的代码出现了空指针的异常 在这里我们也可以模拟异常
                subscriber.onError(new NullPointerException("空指针异常"));
                //只要抛出了异常 onErrorResume就会被调用
                //跟上面的onErrorResumeNext()效果一样都是返回一个
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        })
                .onExceptionResumeNext(Observable.just(666))
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"onNext():"+integer);
                    }
                });
    }
}
