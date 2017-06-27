package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.ConnectableObservable;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

/**
 * Created by Administrator on 2017/6/26.
 */

public class RxDemoTest {

    private static final String TAG ="RxDemoTest" ;

    public static void test1(){
        //1.报社-----》被观察者Observable(可观察的)
        Observable<String> observable=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //相当于发报纸
                subscriber.onNext("hello andorid!");
                subscriber.onNext("hello liuchen8899!");
                //每次完毕之后，最好调用onCompleted()告诉观察者，发送事件完毕了
               // subscriber.onCompleted();

                //官方推荐 只要调用onCompleted()那么后面的事件就不会被接收到了！
                //模拟一个空指针
                subscriber.onError(new NullPointerException("空指针错误！！！！"));
                //调用onError()之后，后面的事件将不会得到响应！
                subscriber.onNext("hello andorid!");
            }
        });
        //2.家庭----》观察者Observer
        Observer<String> observer=new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted()----");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"onError()----"+e.getLocalizedMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG,"onNext()----"+s);
            }
        };

        //3.关联-----》订阅subscribe
        //4.原理，只要调用subscribe()，系统内部就会调用观察者Observable的call()接着就会让被观察者发送事件
        //观察者就能接收对应的事件
        observable.subscribe(observer);
    }

    public static void test2(){

        Single<String> observable=Single.create(new Single.OnSubscribe<String>() {

            //调用onSuccess()
            //系统内部调用观察者对象的onNext(),onComplete()方法
            @Override
            public void call(SingleSubscriber<? super String> subscriber) {
               // subscriber.onSuccess("好好学习！");
                subscriber.onError(new NullPointerException("空指针异常！！！"));
            }
        });

        Observer<String> observer=new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted()----");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"onError()----"+e.getLocalizedMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG,"onNext()----"+s);
            }
        };
        observable.subscribe(observer);
    }


    public static void test3(){
        //1.报社-----》被观察者Observable(可观察的)
        Observable<String> observable=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //相当于发报纸
                subscriber.onNext("hello andorid!");
                subscriber.onNext("hello liuchen8899!");
                //每次完毕之后，最好调用onCompleted()告诉观察者，发送事件完毕了
                // subscriber.onCompleted();

                //官方推荐 只要调用onCompleted()那么后面的事件就不会被接收到了！
                //模拟一个空指针
                subscriber.onError(new NullPointerException("空指针错误！！！！"));
                //调用onError()之后，后面的事件将不会得到响应！
                subscriber.onNext("hello andorid!");
            }
        });
        //2.家庭----》观察者Observer
        //Subscriber是observer的子类，它还提供了取消订阅和判断是否订阅的方法
        Subscriber<String> observer=new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted()----");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"onError()----"+e.getLocalizedMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG,"onNext()----"+s);
            }
        };

        //3.关联-----》订阅subscribe
        //4.原理，只要调用subscribe()，系统内部就会调用观察者Observable的call()接着就会让被观察者发送事件
        //观察者就能接收对应的事件
        observable.subscribe(observer);
    }

    public static void test4(){
        //1.报社-----》被观察者Observable(可观察的)
        Observable<String> observable=Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                subscriber.onNext("hello liuchen8899!");

                //如果只关心onNext事件，那么被观察者发送了异常没人处理，就会抛给系统
               // subscriber.onError(new NullPointerException("空指针错误！！！！"));
                subscriber.onCompleted();

            }
        });
        //如果只关心onNext事件，而不关心onComplete(),onError()

        Action1<String> onNextAction=new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call:"+s);
            }
        };

        Action1<Throwable> onErrorAction=new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG,"throwable:"+throwable.getLocalizedMessage());
            }
        };

        Action0 onCompleteAction=new Action0() {
            @Override
            public void call() {
                Log.e(TAG,"onComplete():");
            }
        };


        //对Action的总结：
        //所谓的Action就是只有一个内部方法的Action类，而且方法都没有返回值。并且
        //Action0没有参数
        //Action1只有一个参数
        //Action2有两个参数
        //Action9有九个参数
        //ActionN一般不用


        //对应的技术还有Function
        //所谓的Func内部只有一个方法，并且方法都有返回值
        //返回值的类型就是最有一个泛型指定的数据类型
        //Func是从Func0-Func9()
        new Func1<String,Double>(){

            @Override
            public Double call(String s) {
                return null;
            }
        };

        //3.关联-----》订阅subscribe
        //4.原理，只要调用subscribe()，系统内部就会调用观察者Observable的call()接着就会让被观察者发送事件
        //观察者就能接收对应的事件
        observable.subscribe(onNextAction,onErrorAction,onCompleteAction);
    }

    public static void test5(){
        //1.创建一个被观察者
        //AsyncSubject他在创建之后就可以发送数据（不同订阅之后再发送数据）他只接收最后一个onNext()事件
        //在onCompletes()调用之前的最后一个实例
        //只要没有onComplete被发送，那么观察者就接收不到任何信息
        AsyncSubject<String> observable=AsyncSubject.create();
        observable.onNext("hello android");
        observable.onNext("hello liuchen8899");
        observable.onCompleted();
      //  observable.onError(new NullPointerException("空指针异常！！！"));
        //2.创建一个观察者
        Action1<String> onNextAction=new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call"+s);
            }
        };

        //3.实现订阅
        observable.subscribe(onNextAction);
    }

    public static void test6(){
        /***
         *BehaviorSubject是以订阅方法作为分界线
         * 只发送订阅前自后一个onNext()事件和订阅后所有的onNext()事件
         * 如果订阅前没有onNext事件，则会调用默认的数据
         */
        BehaviorSubject<String> observable= BehaviorSubject.create("Default");
//        observable.onNext("hello android(A)");
//        observable.onNext("hello android(B)");
//        observable.onNext("hello android(C)");

        // observable.onCompleted();
        //observable.onError(new NullPointerException("空指针异常！！！"));
        //2.创建一个观察者
        Action1<String> onNextAction=new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call()--"+s);
            }
        };

        //3.实现订阅
        observable.subscribe(onNextAction);

        observable.onNext("hello liuchen8899(D)");
        observable.onNext("hello liuchen8899(E)");
        observable.onNext("hello liuchen8899(F)");
    }

    public static void test7(){
        /***
         *replatSubject刚创建完毕的时候就可以发送数据了
         * 不管观察者是什么时候订阅，它都会接受ReplaySubject对象发出的任何事件。
         */
        PublishSubject<String> observable= PublishSubject.create();
        observable.onNext("hello android(A)");
        observable.onNext("hello android(B)");
        observable.onNext("hello android(C)");

        // observable.onCompleted();
        //observable.onError(new NullPointerException("空指针异常！！！"));
        //2.创建一个观察者
        Action1<String> onNextAction=new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call()--"+s);
            }
        };

        //3.实现订阅
        observable.subscribe(onNextAction);

        observable.onNext("hello liuchen8899(D)");
        observable.onNext("hello liuchen8899(E)");
        observable.onNext("hello liuchen8899(F)");
    }

    public static void test8(){
        /***
         *创建一个被观察者PublishSubject ；他在创建之后就可以发送数据
         * 只有订阅调用之后，观察者才能拿到订阅后的事件
         * 作为观察者只能接收到订阅后的所有事件
         */
        ReplaySubject<String> observable= ReplaySubject.create();
        observable.onNext("hello android(A)");
        observable.onNext("hello android(B)");
        observable.onNext("hello android(C)");

        // observable.onCompleted();
        //observable.onError(new NullPointerException("空指针异常！！！"));
        //2.创建一个观察者
        Action1<String> onNextAction=new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call()--"+s);
            }
        };

        //3.实现订阅
        observable.subscribe(onNextAction);

        observable.onNext("hello liuchen8899(D)");
        observable.onNext("hello liuchen8899(E)");
        observable.onNext("hello liuchen8899(F)");
    }

    /**
     * 可连接的观察者可以定时发布订阅消息
     *
     */
    public static void test9() {
        //1.报社-----》被观察者Observable(可观察的)
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //相当于发报纸
                subscriber.onNext("hello andorid!");
                subscriber.onNext("hello liuchen8899!");
                //每次完毕之后，最好调用onCompleted()告诉观察者，发送事件完毕了
                // subscriber.onCompleted();

                //官方推荐 只要调用onCompleted()那么后面的事件就不会被接收到了！
                //模拟一个空指针
                subscriber.onError(new NullPointerException("空指针错误！！！！"));
                //调用onError()之后，后面的事件将不会得到响应！
                subscriber.onNext("hello andorid!");
            }
        });

        //将一个普通的被观察者变成可连接的观察者
        ConnectableObservable<String> connectableObservable = observable.publish();


        //将一个可连接的观察者变成一个普通的被观察者
        Observable<String> observable1=connectableObservable.refCount();


        //2.家庭----》观察者Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted()----");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError()----" + e.getLocalizedMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext()----" + s);
            }
        };

        //3.关联-----》订阅subscribe
        //4.原理，只要调用subscribe()，系统内部就会调用观察者Observable的call()接着就会让被观察者发送事件
        //观察者就能接收对应的事件
        connectableObservable.subscribe(observer);

        //让可链接的被观察者调用内部的call()方法（相当于发送事件）
        connectableObservable.connect();
    }

    public static void test10() {
        //1.报社-----》被观察者Observable(可观察的)
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //相当于发报纸
                subscriber.onNext("hello andorid!");
                subscriber.onNext("hello liuchen8899!");
                //每次完毕之后，最好调用onCompleted()告诉观察者，发送事件完毕了
                // subscriber.onCompleted();

                //官方推荐 只要调用onCompleted()那么后面的事件就不会被接收到了！
                //模拟一个空指针
               // subscriber.onError(new NullPointerException("空指针错误！！！！"));
                //调用onError()之后，后面的事件将不会得到响应！
                subscriber.onNext("hello andorid!");
            }
        });

        //将一个普通的被观察者变成可连接的观察者

        //pubsilh()创建的观察者之后再cinnect()之前订阅的观察者才能接手事件
        //在connect()之后订阅的观察者是无法获取被观察者发送的事件

        //有没有办法让只要是观察者订阅了可连接的被观察者都能打印出被观察者发送出来的数据
        //不管订阅是在connect()的前后顺序
        //这个函数叫做Replay()
        ConnectableObservable<String> connectableObservable = observable.replay();


        //实现订阅
        connectableObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call1()---"+s);
            }
        });


        connectableObservable.connect();

        //再次订阅一个新的观察者
        connectableObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call2()---"+s);
            }
        });
    }

    public void test11(){
        //有的观察者 在创建之后就马上发送了数据 ======>热Observable subject的子类
        //有的观察者 在订阅的时候才发送数据------->冷Observable //普通的observable
        //还有一种特俗的观察者 他可以在我们指定的时间点发送数据------>冷Observable //可连接的observable
    }
}
