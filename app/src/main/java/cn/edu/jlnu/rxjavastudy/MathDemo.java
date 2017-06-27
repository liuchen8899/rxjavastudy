package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import rx.Observable;
import rx.functions.Action1;
import rx.observables.MathObservable;

/**
 * Created by Administrator on 2017/6/26.
 */

public class MathDemo {

    private static final String TAG ="MathDemo" ;

    public  static void test1(){
        Observable<Integer> observable=Observable.just(1,2,3,4,5,6);
        Observable<Integer> mathObservable=MathObservable.averageInteger(observable);
        mathObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"call:"+integer);
            }
        });

    }

    public  static void test2(){
        Observable<Integer> observable=Observable.just(1,2,3,4,5,6);
        Observable<Integer> mathObservable= //MathObservable.max(observable); //求数据的最大值
        MathObservable.min(observable); //求数据的最大值
        //
        mathObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"call:"+integer);
            }
        });

    }

    public  static void test3(){
        Observable<Integer> observable=Observable.just(1,2,3,4,5,6);
        Observable<Integer> mathObservable=MathObservable.sumInteger(observable);
        mathObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"call:"+integer);
            }
        });

    }
    public  static void test4(){
        Observable<Integer> observable=Observable.just(1,2,3,4,5,6);
        Observable<Integer> mathObservable=observable.count();
        mathObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"call:"+integer);
            }
        });

    }
}
