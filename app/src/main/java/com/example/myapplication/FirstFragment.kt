package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.app.AlertDialog
import android.content.DialogInterface


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var encryptButton: Button? = null
    private var decryptButton: Button? = null
    private var key: EditText? = null
    private var text: EditText? = null

    private val vizener = Vizener()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        encryptButton = view.findViewById<Button>(R.id.encryptBtton)
        key = view.findViewById<EditText>(R.id.key)
        text = view.findViewById<EditText>(R.id.text)
        decryptButton = view.findViewById<Button>(R.id.decryptButton)


        encryptButton!!.setOnClickListener {
            if(key!!.text.toString() != "") {
                val encrypted = vizener.encrypt(text!!.text.toString(), key!!.text.toString())
                text!!.setText(encrypted);
            } else {
                val dialogBuilder = AlertDialog.Builder(this.context)

                dialogBuilder.setMessage("Kljuc ne moze biti prazan")
                    .setCancelable(false)
                    .setPositiveButton("OK", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })

                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Error")
                // show alert dialog
                alert.show()
            }
        }

        decryptButton!!.setOnClickListener {
            val decrypted = vizener.decrypt(text!!.text.toString())
            text!!.setText(decrypted.a)
            key!!.setText(decrypted.b)
        }
    }
}
