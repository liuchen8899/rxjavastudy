package cn.edu.jlnu.rxjavastudy;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/6/27.
 */

public class TransformDemo {
    private static final String TAG = TransformDemo.class.getSimpleName();

    public static void test1(){
        //被观察者与观察者可能存在不同的线程之间 有的时候可能会导致被观察者发送的数据比较快
        //从而 观察者来不及处理就被抛弃了
        //如果使用了 buffer函数可以帮助我们”减少“此类事件的发生
        Observable observable=Observable.just("hello android!","hello java!","hello other");
        Observable<List<String>> bufferObservable=observable.buffer(2);
        bufferObservable.subscribe(new Action1<List<String>>() {

            @Override
            public void call(List<String> list) {
                Log.e(TAG,"--------------------");
               for(int i=0;i<list.size();i++){
                   Log.e(TAG,"call "+list.get(i));
               }

            }
        });
    }

    public static void test2(){

        //window就是将一个整体的被观察者分解成不同的子观察者
        //再根据不同的子观察者进行处理
        Observable observable=Observable.just("hello android!","hello java!","hello other");
        Observable<Observable<String>> windowObservable=observable.window(2);
        windowObservable.subscribe(new Action1<Observable<String>>() {


            @Override
            public void call(Observable<String> observable) {
                Log.e(TAG,"--------------------");
               observable.subscribe(new Action1<String>() {
                   @Override
                   public void call(String s) {
                       Log.e(TAG,"call "+s);
                   }
               });
            }
        });
    }

    public static void test3(){
        Observable.from(initStudents())
                .map(new Func1<Student, Student>() {
                    @Override
                    public Student call(Student student) {
                        if(student.getName().equals("张三")){
                            student.setName("张三丰");
                        }
                        return student;
                    }
                })
                .subscribe(new Action1<Student>() {
                    @Override
                    public void call(Student student) {
                        Log.e(TAG,"student----> "+student);
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

}
