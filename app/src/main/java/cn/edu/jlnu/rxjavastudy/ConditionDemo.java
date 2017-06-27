package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/6/26.
 */

public class ConditionDemo {
    private static String TAG="ConditionDemo";
    private static List<Student> initStudents() {
        ArrayList<Student> students=new ArrayList<>();
        students.add(new Student("张三",12));
        students.add(new Student("李四",13));
        students.add(new Student("王五",14));
        students.add(new Student("张三",14)); //不小心将张三多录一次
        return students;
    }

    public static void test1(){
        //需求：学校需要组织一个篮球队 年龄小于等于20岁，判断所有的学生 是否都满足条件
        //只要有一个学生不满足就返回false
        Observable.from(initStudents())
                .all(new Func1<Student, Boolean>() {
                    @Override
                    public Boolean call(Student student) {
                        return student.getAge()<=20;
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean b) {
                        Log.e(TAG,"call:"+b);
                    }
                });
    }

    public static void test2(){
        //1.创建A被观察者
        Observable<String> observableA=Observable.just("A同学举手了","A同学回答了问题");
        //1.创建B被观察者
        Observable<String> observableB=Observable.just("B同学举手了","B同学回答了问题");

        //amb会发送先发送数据的被观察者的所有数据
        //后面的被观察者发送的数据会被忽略掉
        //抢占资源 只要抢占成功，后面的资源就由
        Observable<String> ambObservable=Observable.amb(observableA,observableB);
        ambObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call:"+s);
            }
        });

    }

    public static void test3(){
        Observable observable=Observable.just(1,2,3,4);
        //判断一个队列中是否存在某个元素
        Observable<Boolean> booleanObservable=observable.contains(3);
        booleanObservable.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                Log.e(TAG,"call:"+aBoolean);
            }
        });
    }

    public static void test4(){
        //需求：从网络上读取一个字符串 如果找不到字符串 则返回一个默认的值
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                //总结如果被观察者有发送数据  则观察者直接接收数据
                //如果被观察者不发送数据，而直接调用onComplete()方法，系统会自动使用defaultEmpty里面默认值
                String result="从网络上读取的字符串";
                //.onNext(result);
                subscriber.onCompleted();
            }
        })
        .defaultIfEmpty("显示默认的值")
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call:"+s);
            }
        });
    }

    public static void test5(){
        //创建两个被观察者对象
        Observable<Integer> observableA=Observable.just(1,2,3,4);
        Observable<Integer> observableB=Observable.just(1,2,3,4);


        //如果发送的元素稍微有点不同就会返回false或者顺序不一样也会返回false
        Observable<Boolean> booleanObservable=Observable.sequenceEqual(observableA,observableB);  //使用sequenceEqual函数 传入的参数顺序无所谓
                booleanObservable.subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        Log.e(TAG,"call:"+aBoolean);
                    }
                });
    }

    public static void test6(){
        Observable<Integer> observableA=Observable.just(1,2,3,4,5,2);
        observableA
                .skipWhile(new Func1<Integer, Boolean>() {

                    //skip=跳过 跳过发送的每一个数据
                    //知道这个条件为false
                    @Override
                    public Boolean call(Integer integer) {
                        return integer!=4;
                    }
                })
                .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"test6:"+integer);
            }
        });
    }

    public static void test7(){
        Observable<Integer> observableA=Observable.just(1,2,3,4,5,2);
        observableA
                .takeWhile(new Func1<Integer, Boolean>() {

                    //take=取拿 接收发送的每一个数据
                    //直到这个条件为false才开始停止让观察者接收发过来的数据
                    @Override
                    public Boolean call(Integer integer) {
                        return integer!=4;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"test6:"+integer);
                    }
                });
    }

    public static void test8(){

    }
}
