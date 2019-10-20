package com.sunny.tinkertest.tablayout

import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sunny.tinkertest.R
import kotlinx.android.synthetic.main.test.*

/**
 * Created by SunShuo.
 * Date: 2019-10-12
 * Time: 17:12
 */
class MyFragment : Fragment() {

    companion object{
        fun newFragment(title: String) : MyFragment{
            val fragment = MyFragment()
            val bundle = Bundle()
            bundle.putString("title",title)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.test, null)
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = arguments?.getString("title")
        return view
    }


}