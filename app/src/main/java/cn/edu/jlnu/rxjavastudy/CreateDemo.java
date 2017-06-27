package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/6/26.
 */

public class CreateDemo {
    private static final String TAG ="CreateDemo" ;

    public static void test1(){
        Observable<Integer> observable=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {

            }
        });

        Observer<Integer> observer=new Observer<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {

            }
        } ;

      //  observable.subscribe(observer);

        /**********************************************/
        Observable.create(new Observable.OnSubscribe<Integer>() {

            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if(!subscriber.isUnsubscribed()){
                    //如果当前的观察者订阅
                    for(int i=0;i<5;i++){
                        subscriber.onNext(i);
                    }
                    subscriber.onCompleted();
                }
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.e(TAG,"onCompleted---");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.e(TAG,"onNext---"+integer);
            }
        });
    }

    public static void  test2(){
        //defer方法创建的被观察者实际上没什么用 他在订阅的时候 在内部创建并发送一个新的观察者供订阅的观察者关联
        //每关联一个观察者对象 那么特俗的被观察者内部就会创建多一个被观察者供关联
        Observable<Integer> observable=Observable.defer(new Func0<Observable<Integer>>() {
            //该方法什么时候调用
            @Override
            public Observable<Integer> call() {
                Log.e(TAG,"call()----");
                Observable<Integer> observable1=Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        subscriber.onNext(123);
                    }
                });
                return observable1;
            }
        });
        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"call 1 ()----"+integer);
            }
        });

        observable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG,"call() 2 ----"+integer);
            }
        });
    }

    public static void test3(){
        Observable
//                .create(new Observable.OnSubscribe<String>() {
//
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                if(!subscriber.isUnsubscribed()){
//                    subscriber.onNext("人丑就要多读书！");
//                    subscriber.onNext("精通安卓必成高富帅！");
//                    subscriber.onCompleted();
//                }
//            }
//        })
                .just("人丑就要多读书","精通安卓必成高帅富")
                .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG,"call()---"+s);
            }
        });
    }

    public static void test4(){
//        List<Student> students=initStudents();
//        for(int i=0;i<students.size();i++){
//            Log.e(TAG,students.get(i).toString());
//        }
        Observable.from(initStudents()) //相当于发送了队列里面的每个元素
                .subscribe(new Action1<Student>() {
                    @Override
                    public void call(Student student) {
                        Log.e(TAG,"call:"+student);
                    }
                });
    }

    private static List<Student> initStudents() {
        ArrayList<Student> students=new ArrayList<>();
        students.add(new Student("张三",12));
        students.add(new Student("李四",13));
        students.add(new Student("王五",14));
        students.add(new Student("张三",34)); //不小心将张三多录一次
        return students;
    }

    public static void test5(){
        //每三秒打印一句话
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                 Log.e(TAG,"run:"+Thread.currentThread().getName());
//            }
//        },3000,3000);
        Observable
                .interval(3, TimeUnit.SECONDS, Schedulers.io())
             //   .timer(3, TimeUnit.SECONDS, Schedulers.io())
         .subscribe(new Action1<Long>() {
             @Override
             public void call(Long aLong) {
                 //along就是计算器，它制定了当前执行方法的次数
                 Log.e(TAG,"run:"+Thread.currentThread().getName());
             }
         });
    }
    public static void test6(){
        //想从1数到10
//        for(int i=0;i<10;i++){
//            Log.e(TAG,"test6:"+i);
//        }
//        Observable.range(4,10)
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer integer) {
//                        Log.e(TAG,"test6:"+integer);
//                    }
//                });
        //将被观察者发送的数据重复发送多次
        Observable.just("人丑就要多读书")
                .repeat(3)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG,"test6:"+s);
                    }
                });
    }

    public static void  test7(){
        Observable.just("起床","洗脸","刷牙")
                .delay(3,TimeUnit.SECONDS) //让被观察者发送的所有数据整体延迟三秒
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG,"test7:"+s);
                    }
                });
    }

    public static void test8(){
        Observable.from(initStudents()) //相当于发送了队列里面的每个元素
                .distinct(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        //根据学生的
                        return student.getName();
                    } //根据某种规则来判断 选项是否重复

                })
                .subscribe(new Action1<Student>() {
                    @Override
                    public void call(Student student) {
                        Log.e(TAG,"call:"+student);
                    }
                });
    }

    public static void test9(){
        //需求：从1打印9
        //过滤函数，根据返回的boolean来决定是否要继续发送
        Observable.range(1,9)
                .filter(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer>5;
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG,"test9:"+integer);
                    }
                });
    }

    public static void test10(){
        Observable.from(initStudents())
                .distinct(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                //.first()
                .elementAt(1) //指定拿到队列中某个索引的数据，如果数据越界就会直接报异常！
               // .last()
                .subscribe(new Action1<Student>() {
                    @Override
                    public void call(Student student) {
                        Log.e(TAG,"test10:"+student);
                    }
                });
    }
    public static void test11(){
        Observable.from(initStudents())
                //.take(3) //获取集合前三个
                .takeLast(2) //获取集合最后两个元素
                .subscribe(new Action1<Student>() {
                    @Override
                    public void call(Student student) {
                        Log.e(TAG,"test11:"+student);
                    }
                });
    }
    public static void  test12(){

    }
}
class Student{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student() {

    }
}
