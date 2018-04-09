package com.meetcity.moon.learn.rxjava;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by moon on 2018/3/8 .
 */
public class RxJavaTest {

    public static void main(String[] args) {

        haha();
        Flowable.just("hello world").subscribe(System.out::println);

        Flowable.just("hahaha").subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });
    }

    public static void haha() {
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                System.out.println("被观察这 subscribe");
                emitter.onNext("nivb");
            }
        });
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("dingyue");
            }

            @Override
            public void onNext(String s) {
                System.out.println("接受到到" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                System.out.println("完成");
            }
        };

        observable.subscribe(observer);
    }


}
