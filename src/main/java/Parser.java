import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Создаем приложение "Погода" на Java [GeekBrains]
 created in 7:00*/
public class Parser {
    //7:15
    public static Document getPage() throws IOException {
        //8:30 як нам скачати сторінку?
        String url = "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    public static void main(String[] args) throws Exception {
        //10:00 перевіримо чи виводиться наш page
        //System.out.println(getPage());

        //14:45 отримаємо спочатку все з конкретного блока -------------------------->
        Document page = getPage();
        //css query language
        Element tableWth = page.select("table[class=wt]").first(); //з таблиці page бажаємо достати великий table з ідентифікатором класа wt, перший
        //System.out.println(tableWth);   //<-------------------------------------------

        //17:15 тепер залишилось данні розділити та вивести на екран те що потрібно
        //19:30
        Elements names = tableWth.select("tr[class=wth]");  //=5
        //20:40 тепер нам потрібно не тільки names отримати, а й values
        Elements values = tableWth.select("tr[valign=top]");    //=19

        //32:10 тепер залишилось розмістити данні між цими рядками
        //32:50 створимо якийсь індекс, він буде рахувати на якому рядку ми зарраз знаходимось
        int index = 0;
        //21:50 а тепер нам потрібно це все виводити
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            //30:30
            String date = getDateFromString(dateString);
            //11:30
            System.out.println(date + "     Явище      Температура     Тиск    Вологість   Вітер"); //09.10 Среда погода сегодня     Явище      Температура     Тиск    Вологість   Вітер
            //36:20
            //printPartValues(values, index);
            //56:00
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;
        }
        /*//12:10
        String date = "";*/
    }

    //26:40 нам потрібно створити патерн - тобтот шаблон по якому потрібно шукати
    //09.10 Среда погода сегодня
    //09.10
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    //23:40 нам бажалось щоб просто було 09.10 !!!!!самий вірний варіант - регулярні вирази !!! Напишемо цілий метод
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can`t extract date from string!");
    }

    //33:30 ми хочемо виводити значення починаючи з цього індекса 4-и значення
    private static int printPartValues(Elements values, int index) {       //54:00 void --> int
        //43:50 давайте обробляти випадок з сьогоднішнім днем окремо ------->
        int iterationCount = 4;
        if (index == 0) { //я вже переробив, при index == 0 це обробка 1-го блока (утро,день,вечер,ночь   день,вечер,ночь    вечер,ночь   ночь)
            Element valueLn = values.get(3);

            ///*I rewoked*/*******************************************************>
           /*  //давайте обробляти тільки випадок з РАНКОМ !
           boolean isMorning = valueLn.text().contains("Утро"); //Вечер Ночь
            if (isMorning) {
                iterationCount = 3;
            }*/

            if (valueLn.text().contains("Утро")) {
                iterationCount = 3;
            } else if (valueLn.text().contains("Ночь")) {
                iterationCount = 2;
            } else if (valueLn.text().contains("Вечер")) {
                iterationCount = 1;
            }
            //<****************************************************

           /* for (int i = 0; i < iterationCount; i++) {  //48:00
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "    ");     //День    Пасмурно. Небольшой дождь.    +4..+6    748    91%    [Ю-З] 1-3 м/с
                }
                System.out.println();   //49:30
            }*/

        }//<------
        else {   //50:45 insert word 'else'
            /*for (int i = 0; i < iterationCount; i++) {
                Element valueLine = values.get(index);
                //38:20 виводимо не весь html-код, а тільки текст з td
                for (Element td : valueLine.select("td")) {
                    System.out.print(td.text() + "    ");     //День    Пасмурно. Небольшой дождь.    +4..+6    748    91%    [Ю-З] 1-3 м/с
                }
                System.out.println();   //49:30
            }*/
        }

        for (int i = 0; i < iterationCount; i++) {  //48:00
            Element valueLine = values.get(index + i);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "    ");     //День    Пасмурно. Небольшой дождь.    +4..+6    748    91%    [Ю-З] 1-3 м/с
            }
            System.out.println();   //49:30
        }

        return iterationCount;         //55:10
    }
}