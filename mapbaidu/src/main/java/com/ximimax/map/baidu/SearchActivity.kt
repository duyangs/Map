package com.ximimax.map.baidu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.NumberPicker
import android.widget.Toast
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.search.poi.*
import kotlinx.android.synthetic.main.ac_search.*

/**
 * <p>Project:Map</p>
 * <p>Package:com.ximimax.map.baidu</p>
 * <p>Description:</p>
 * <p>Company:</p>
 *
 * @author ryandu
 * @date 2018/7/22
 */
class SearchActivity : AppCompatActivity() {

    private var mPoiSearch = PoiSearch.newInstance()
    private var searchType = POI_SEARCH

    private var pageNum = 0

    private var searchAdapter: SearchAdapter? = null

    private var cityName = "上海"

    private var canBeLoaded = true //是否可加载更多

    companion object {

        private const val CITY_NAME = "cityName"

        private const val POI_SEARCH = 0
        private const val SUG_SEARCH = 1
        private const val BUS_SEARCH = 2
        private const val GEN_SEARCH = 3
        private const val DISTRICT_SEARCH = 4
        private const val LBS_SEARCH = 5
        private const val RGC_SEARCH = 6

        fun start(context: Context, cityName: String) {
            var intent = Intent(context, SearchActivity::class.java)
            var bundle = Bundle()
            bundle.putString(CITY_NAME, cityName)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_search)
        initView()
    }

    private fun initView() {
        cityName = intent.extras.getString(CITY_NAME)
        setTitle("POI")
        initSearchListener()
        searchAdapter = SearchAdapter(this)
        tv_search.setOnClickListener {
            searchAdapter!!.clearData()
            pageNum = 0
            onSearch()
        }
        lv_info.adapter = searchAdapter
        lv_info.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                if (firstVisibleItem == 0) {
                    val firstVisibleItemView = lv_info.getChildAt(0)
                    if (firstVisibleItemView != null && firstVisibleItemView!!.top == 0) {
                        Log.d("ListView", "##### 滚动到顶部 #####")
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    val lastVisibleItemView = lv_info.getChildAt(lv_info.childCount - 1)
                    if (lastVisibleItemView != null && lastVisibleItemView!!.bottom == lv_info.height) {
                        Log.d("ListView", "##### 滚动到底部 ######")
                        if (canBeLoaded) {
                            pageNum++
                            onSearch()
                        } else {
                            Toast.makeText(this@SearchActivity, "无更多数据", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
            }

        })
    }

    private fun setTitle(type:String){
        title = "$cityName ($type)"
    }

    private fun initSearchListener() {
        mPoiSearch.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
            override fun onGetPoiIndoorResult(result: PoiIndoorResult?) {
                Log.d("PoiIndoorResult", result.toString())
            }

            override fun onGetPoiResult(result: PoiResult?) {
                if (result!!.allPoi.size > 0) {
                    searchAdapter!!.addData(result.allPoi)
                }
                canBeLoaded = result.allPoi.size >= 10
            }

            override fun onGetPoiDetailResult(result: PoiDetailResult?) {
                Log.d("PoiDetailResult", result.toString())
            }

        })
    }

    private fun onSearch() {
        when (searchType) {
            POI_SEARCH -> poiSearch()
            SUG_SEARCH -> sugSearch()
            BUS_SEARCH -> busSearch()
            GEN_SEARCH -> genSearch()
            DISTRICT_SEARCH -> districtSearch()
            LBS_SEARCH -> lbsSearch()
            RGC_SEARCH -> rgcSearch()
        }
    }

    private fun poiSearch() {
        setTitle("POI")
        mPoiSearch.searchInCity(PoiCitySearchOption().city(cityName).keyword(et_keyword.text.toString()).pageNum(pageNum).pageCapacity(10))
    }

    private fun sugSearch() {
        setTitle("SUG")
        Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show()
    }

    private fun busSearch() {
        setTitle("BUS")
        Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show()
    }

    private fun genSearch() {
        setTitle("GEN")
        Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show()
    }

    private fun districtSearch() {
        setTitle("DISTRICT")
        Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show()
    }

    private fun lbsSearch() {
        setTitle("LBS")
        Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show()
    }

    private fun rgcSearch() {
        setTitle("RGC")
        Toast.makeText(this, "暂未开放", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPoiSearch.destroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        searchType = when (item!!.itemId) {
            R.id.action_poi -> POI_SEARCH
            R.id.action_sug -> SUG_SEARCH
            R.id.action_bus -> BUS_SEARCH
            R.id.action_gen -> GEN_SEARCH
            R.id.action_district -> DISTRICT_SEARCH
            R.id.action_lbs -> LBS_SEARCH
            R.id.action_rgc -> RGC_SEARCH
            else -> POI_SEARCH
        }
        onSearch()
        return true
    }
}