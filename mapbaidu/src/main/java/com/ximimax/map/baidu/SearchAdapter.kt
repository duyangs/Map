package com.ximimax.map.baidu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.baidu.mapapi.search.core.PoiInfo

/**
 * <p>Project:Map</p>
 * <p>Package:com.ximimax.map.baidu</p>
 * <p>Description:</p>
 * <p>Company:</p>
 *
 * @author ryandu
 * @date 2018/7/22
 */
class SearchAdapter(context: Context) : BaseAdapter() {

    private var context: Context? = context
    private var mList: MutableList<PoiInfo> = mutableListOf()


    companion object {
        class ViewHolder {
            var textView: TextView? = null
        }
    }

    fun addData(list: List<PoiInfo>){
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData(){
        mList.clear()
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var holder:ViewHolder? = null
        var mConvertView = convertView
        if (null == mConvertView) {
            mConvertView = inflater.inflate(R.layout.item_search, null)
            holder = ViewHolder()
            holder.textView =  mConvertView.findViewById(R.id.tv_name)
            mConvertView.tag = holder
        }    else {
            holder = mConvertView.tag as ViewHolder?
        }

        holder!!.textView!!.text = getItem(position).name
        return mConvertView!!
    }

    override fun getItem(position: Int): PoiInfo {
        return mList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mList.size
    }


}