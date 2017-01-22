import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import org.json.*;

/**
 *
 * @author boyuantan
 */
public class Shopicruit {

    /**
     * @param args the command line arguments
     */
    final static String BASE_URL = "https://shopicruit.myshopify.com/admin/orders.json?page=";
    final static String ACCESS_TOKEN = "&access_token=c32313df0d0ef512ca64d5b336a0d7c6";

    public static void main(String[] args) {
        /*
        Consider:
            - Cancelled orders/Test orders
            - Different Currencies used to purchaes order
         */

        int page = 1;
        BigDecimal totalPrice = new BigDecimal("0.0");
        JSONArray orderArr = null;
        try {

            do {
                URL orders = new URL(BASE_URL + page + ACCESS_TOKEN);
                BufferedReader input = new BufferedReader(new InputStreamReader(orders.openStream()));

                String orderString = "";
                String inputLine;

                while ((inputLine = input.readLine()) != null) {
                    orderString += inputLine;
                }

                JSONObject orderObj = new JSONObject(orderString);
                orderArr = orderObj.getJSONArray("orders");

                //no orders were cancelled/test orders/etc. , and all prices given were in CAD;
                //so this can be removed but could also remain here without any major consequence
                for (int i = 0; i < orderArr.length(); i++) {
                    JSONObject currOrder = orderArr.getJSONObject(i);
                    if (currOrder.isNull("cancelled_at")) {
                        double price = orderArr.getJSONObject(i).getDouble("total_price");
                        totalPrice = totalPrice.add(new BigDecimal(Double.toString(price)));
                    }
                }
                input.close();
                page++;
            } while (0 < orderArr.length());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.print("$" + totalPrice.toString());
    }

}
