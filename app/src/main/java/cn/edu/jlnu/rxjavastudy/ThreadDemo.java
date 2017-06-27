package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/27.
 */

public class ThreadDemo {
    private static final String TAG =ThreadDemo.class.getSimpleName() ;

    public static void test1(){
        //默认情况下下 被观察者与观察者运行在他调用的线程中

        //observeOn //观察者运行所在的线程
       Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello android!");
                Log.e(TAG,"被观察者："+Thread.currentThread().getName());
            }
        }).observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"called:"+s);
                Log.e(TAG,"观察者："+Thread.currentThread().getName());
            }
        });
    }

    public static void test2(){
        //默认情况下下 被观察者与观察者运行在被观察者调用的线程中

        //observeOn //观察者运行所在的线程

        //subscribeOn 可以让观察者与被观察者运行在指定的线程中

        //io 执行IO密集的操作 比如执行网络操作 文件流 数据库操作

        //AndroidSchedulers.mainThread() 安卓的主线程

        //Schedulers.newThread() 创建一个普通的线程

        //观察者用来处理网络请求
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello android!");
                Log.e(TAG,"被观察者："+Thread.currentThread().getName());
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"called:"+s);
                Log.e(TAG,"观察者："+Thread.currentThread().getName());
            }
        });
    }

    public static void test3(){
       final Scheduler.Worker worker= Schedulers.io().createWorker(); //在某个调度器中创建一个任务 此刻 worker=runnable
        //执行Schedule实际上就会调用内部的call()方法

        worker.schedule(new Action0() {
            @Override
            public void call() {
                Log.e(TAG,"当前线程call():"+Thread.currentThread().getName());
                //一般情况下哎 当子线程中执行完任务之后  为了不让内存浪费 都需要进行注销 取消订阅
                worker.unsubscribe();
            }
        });
    }
}
