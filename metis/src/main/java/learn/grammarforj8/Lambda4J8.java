package learn.grammarforj8;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by wds on 2017/11/2.
 *
 * @author moon
 */
public class Lambda4J8 {

    public static void main(String args[]){

        List<String> names = Arrays.asList("Lay","Li","Bob","Ming","To","LL","Moon");

    }


    public static void lambda(Map<String,Integer> map,String page){

        //取代匿名类
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        new Thread(()-> System.out.println()).start();

        Runnable runnable = ()-> System.out.println("");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        };
        ActionListener listener1 = e -> System.out.println("");
        Runnable runnable1 = () -> {
            System.out.println("");
            System.out.println("");
        };

        List<String> list = Arrays.asList("Loo","Moo","Boo");
        for(String str: list){

        }
        list.stream().filter(s -> s.equals("Moon")).forEach(s -> System.out.println(s));

        Set<String> set = list.stream().filter(s -> !Character.isDigit(s.charAt(0)))
                .map(String::toUpperCase)
                .collect(Collectors.toSet());
    }
}
