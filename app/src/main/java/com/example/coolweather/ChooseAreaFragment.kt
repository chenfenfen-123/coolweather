package com.example.coolweather

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.coolweather.db.City
import com.example.coolweather.db.County
import com.example.coolweather.db.Province
import com.example.coolweather.util.HttpUtil
import com.example.coolweather.util.Utility
import okhttp3.Call
import okhttp3.Response
import org.litepal.LitePal
import org.litepal.crud.LitePalSupport
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChooseAreaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChooseAreaFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var progressDialog : ProgressDialog? = null
    private lateinit var titleText: TextView
    private lateinit var backButton: Button
    private lateinit var listView: ListView
    private  var mAdapter: ArrayAdapter<String>? = null
    private val dataList = ArrayList<String>()
    private lateinit var provinceList: List<Province>
    private lateinit var cityList: List<City>
    private lateinit var countyList: List<County>
    private lateinit var selectedProvince: Province
    private lateinit var selectedCity: City
    private var currentLevel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_choose_area, container, false)
        titleText = view.findViewById(R.id.title_text)
        backButton = view.findViewById(R.id.back_button)
        listView = view.findViewById(R.id.list_view)
        mAdapter = context?.let { ArrayAdapter(it,android.R.layout.simple_list_item_1,dataList) }
        listView.adapter = mAdapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.setOnItemClickListener { parent, _, positon, _ ->
            when(currentLevel) {
                LEVEL_PROVINCE -> {
                    selectedProvince = provinceList[positon]
                    queryCites()
                }
                LEVEL_CITY -> {
                    selectedCity = cityList[positon]
                    queryCounties()
                }
            }
        }
        backButton.setOnClickListener {
            when(currentLevel) {
                LEVEL_COUNTY -> {
                    queryCites()
                }
                LEVEL_CITY -> {
                    queryProvinces()
                }
            }
        }
        queryProvinces()
    }

    private fun queryCounties() {
        titleText.text = selectedCity.cityName
        countyList = LitePal.where("cityid=?",selectedCity.id.toString()).find(County::class.java)
        if (countyList.isNotEmpty()) {
            dataList.clear()
            for (county in countyList) {
                dataList.add(county.countyName)
            }
            mAdapter?.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel= LEVEL_COUNTY
        }
        queryFromServer("http://guolin.tech/api/china/${selectedProvince.provinceCode}/${selectedCity.cityCode}", COUNTY)
    }

    private fun queryCites() {
        titleText.text = selectedProvince.provinceName
        backButton.visibility = View.VISIBLE
        cityList = LitePal.where("provinceid=?",selectedProvince.id.toString()).find(City::class.java)
        if (cityList.isNotEmpty()) {
            dataList.clear()
            for (city in cityList) {
                dataList.add(city.cityName)
            }
            mAdapter?.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel = LEVEL_CITY
        }
        queryFromServer("http://guolin.tech/api/china/${selectedProvince.provinceCode}", CITY)
    }

    /*
    查询所有的省，先从数据库查询，查不到再到服务器
     */
    private fun queryProvinces() {

        titleText.text = getString(R.string.china)
        backButton.visibility = View.GONE
        provinceList = LitePal.findAll(Province::class.java)
        if (provinceList.isNotEmpty()) {
            dataList.clear()
            for (province in provinceList) {
                dataList.add(province.provinceName)
            }
            mAdapter?.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        } else {
           queryFromServer("http://guolin.tech/api/china",PROVINCE)
        }
    }

    private fun queryFromServer(url: String, type: String) {
        Log.d(TAG,"queryFromServer")
        HttpUtil.sendOkHttpRequest(url, object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(context,"加载失败",Toast.LENGTH_SHORT).show()
                    Log.d(TAG, e.toString())
                }
            }
            override fun onResponse(call: Call, response: Response) {
                val data = response.body?.string() ?: ""
                var result = false
                when(type) {
                    PROVINCE -> {
                        result = Utility.handleProvinceResponse(data)
                    }
                    CITY -> {
                        result = Utility.handleCityResponse(data, selectedProvince.id)
                    }
                    COUNTY -> {
                        result = Utility.handleCountyResponse(data, selectedCity.id)
                    }
                }
                if (result) {
                    activity?.runOnUiThread {
                        closeProgressDialog()
                        when(type) {
                            PROVINCE -> {
                                queryProvinces()
                            }
                            CITY -> {
                                queryCites()
                            }
                            COUNTY -> {
                                queryCounties()
                            }
                        }
                    }
                }
            }

        })
    }

    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity).apply {
                setMessage("正在加载...")
                setCanceledOnTouchOutside(false)
            }
        }
        progressDialog?.show()
    }

    private fun closeProgressDialog() {
        progressDialog?.dismiss()
    }

    companion object {
        private const val LEVEL_PROVINCE = 0
        private const val LEVEL_CITY = 1
        private const val LEVEL_COUNTY = 2
        private const val PROVINCE = "province"
        private const val CITY = "city"
        private const val COUNTY = "county"
        private const val TAG = "ChooseAreaFragment"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChooseAreaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}