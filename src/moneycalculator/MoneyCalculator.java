
package moneycalculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MoneyCalculator {
    public static void main(String[] args) {
        MoneyCalculator moneyCalculator = new MoneyCalculator();
        moneyCalculator.control();
        
    }
    
    private Money money;
    private ExchangeRate exchangeRate;
    private Currency currencyTo;
    private Map<String, Currency> currencies = new HashMap<>();
    
    public MoneyCalculator(){
        currencies.put("USD", new Currency("USD", "Dólar americano", "$"));
        currencies.put("EUR", new Currency("EUR", "Euros", "€"));
        currencies.put("GBP", new Currency("GBP", "Libras Esterlinas", "£"));
    }
    
    private void control() throws IOException {
        input();
        process();
        output();
    }
    
    private void input() {
        System.out.println("Introduzca una cantidad:");
        Scanner scanner = new Scanner(System.in);
        double amount = Double.parseDouble(scanner.next());
        
        System.out.println("Introduzca divisa origen");
        Currency currency = currencies.get(scanner.next().toUpperCase());
        
        money = new Money (amount, currency);
        
        System.out.println("Introduzca divisa destino");
        currencyTo = currencies.get(scanner.next().toUpperCase());
    }
    
    private void process() throws IOException {
        exchangeRate = getExchangeRate(money.getCurrency(), currencyTo);
    }
    
    private void output() {
        System.out.println(money.getAmount() + " "
                + money.getCurrency().getSymbol() + "equivalen a "
                + money.getAmount() * exchangeRate.getRate() + " "
                + currencyTo.getSymbol());
               
    }
    private static ExchangeRate getExchangeRate (Currency from, Currency to) throws IOException {
        URL url = 
            new URL("https://free.currconv.com/api/v7/convert?q=" + from + "_" + to + "&compact=ultra&apiKey=e374ec108bbad0f57f78");
        URLConnection connection = url.openConnection();
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String line = reader.readLine();
            String line1 = line.substring(line.indexOf(to.getCode())+12,line.indexOf("}"));
            return new ExchangeRate(from, to, 
                    Double.parseDouble(line1));
        }
    }
    
}
