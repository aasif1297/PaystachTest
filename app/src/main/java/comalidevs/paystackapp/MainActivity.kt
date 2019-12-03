package comalidevs.paystackapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.paystack.android.Paystack.TransactionCallback
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var cardNumber = ""

    var expiryMonth = 0 //any month in the future


    var expiryYear = 0// any year in the future


    var cvv = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PaystackSdk.initialize(applicationContext);

        val card = Card(edt_card_number.text.toString(), edt_month.text.toString().toInt(), edt_year.text.toString().toInt(), edt_cvv.text.toString())

        if (card.isValid) {
            performCharge(card)
        }else{
            Toast.makeText(this@MainActivity, "invalid", Toast.LENGTH_LONG).show()
        }

    }

    private fun performCharge(card: Card) { //create a Charge object
        val charge = Charge()
        //set the card to charge
        charge.card = card
        //call this method if you set a plan
        //charge.setPlan("PLN_yourplan");
        charge.email = "mytestemail@test.com" //dummy email address
        charge.amount = 100 //test amount
        PaystackSdk.chargeCard(this@MainActivity, charge, object : TransactionCallback {
            override fun onSuccess(transaction: Transaction) { // This is called only after transaction is deemed successful.
                // Retrieve the transaction, and send its reference to your server // for verification.
                val paymentReference = transaction.reference
                Toast.makeText(this@MainActivity, "Transaction Successful! payment reference: $paymentReference", Toast.LENGTH_LONG).show()
            }

            override fun beforeValidate(transaction: Transaction) { // This is called only before requesting OTP.
               // Save reference so you may send to server. If
               // error occurs with OTP, you should still verify on server.
            }

            override fun onError(error: Throwable, transaction: Transaction) { //handle error here
            }
        })
    }
}
