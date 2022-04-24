package processors;

import processing.Processor;
import processing.Status;
import processing.StatusListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AdderProcessor implements Processor {

    private static int taskId = 0;
    private String result = null;

    @Override
    public boolean submitTask(String task, StatusListener sl) {
        taskId++;
        AtomicInteger ai = new AtomicInteger(0);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        // Aby zasymulować długotrwałe przetwarzanie uruchamiane są w nieskończoność
        // krótkie zadania inkrementujące licznik i nie zwracające wartości.
        // Właściwie powinno się przetwarzać przekazywany task, ale tutaj nie jest to robione.
        //
        // ScheduledFuture<?> scheduleFuture = executorService.scheduleAtFixedRate(

        int a,b;

        String arr[] = task.split(" ",3);

        if (arr.length!=3)
        {
            System.out.println("Niepoprawnie zdefiniowane zadanie");
            return false;
        }
        if (!arr[1].equals("+"))
        {
            System.out.println("Niepoprawnie zdefiniowane zadanie");
            return false;
        }

        try {
            a=Integer.parseInt(arr[0]);
            b=Integer.parseInt(arr[2]);

        }
        catch (NumberFormatException e)
        {
            System.out.println("Niepoprawnie zdefiniowane zadanie");
            return false;
        }

        executorService.scheduleAtFixedRate(
                () -> {
                    ai.incrementAndGet();
                    sl.statusChanged(new Status(taskId, ai.get()));
                },
                1, 10, TimeUnit.MILLISECONDS);

        // Powyższe można byłoby zrobić w pętli
        //  for(int i=1; i<=100; i++){
        //     try {
        //         ai.incrementAndGet();
        //        Thread.sleep(1000);
        //     } catch(InterruptedException e){System.out.println(e);}
        //     sl.statusChanged(new Status(taskId,ai.get()));
        // ale zrobiono to inaczej.

        // Ponieważ zapuszczono egzekutor, trzeba poczekać, aż przekręci
        // się w nim 100 zadań, po czym należy zamknąć serwis egzekutora
        // i zakończy działanie samego egzekutora.
        // Można to zrobić "na zewnątrz", w kolejnym egzekutorze.
        // Przykład ten nie jest może najlepszy, ale chodziło w nim
        // o pokazanie synchronizacji przez zmienną współdzieloną.

        ExecutorService executor = Executors.newSingleThreadExecutor();
        // uruchom zadanie, które skończy się, gdy licznik przekroczy wartość 100
        executor.submit(() -> {
            while (true) {
                //System.out.println(scheduleFuture.isDone()); will always print false
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (ai.get() >= 100) {
                    // przekręciliśmy 100 razy,
                    // możemy zwrócić wynik przetwarzania
                    // tutaj "przetwarzanie" polega na zamianie literek na duże
                    result = String.valueOf(a+b);
                    System.out.println("finished");
                    System.out.println(result);
                    //scheduleFuture.cancel(true);
                    executorService.shutdown();
                    executor.shutdown();
                    break;
                }
            }
        });

        // Jeśli chcielibyśmy zaznaczyć, że coś poszło nie tak
        // należałoby zwrócić false
        // (np. gdy ostatnie przetwarzanie jeszcze nie dobiegło końca)
        //
        // Jeśli wszystko poszło ok, wtedy zwracane jest true
        //
        // Uwaga: przypominając - implementacja "przetwarzania" jest asynchroniczna,
        //        zwrócenie wartości true należy interpretować jako informację o tym,
        //        że zlecenie przetwarzania się udało.
        return true;
    }

    @Override
    public String getInfo() {
        return "Dodawanie liczb";
    }

    @Override
    public String getResult() {
        return result;
    }

}