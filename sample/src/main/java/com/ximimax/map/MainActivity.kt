package com.ximimax.map

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.ximimax.map.baidu.EntranceActivity

import com.ximimax.map.R
import kotlinx.android.synthetic.main.ac_main.*
class MainActivity : AppCompatActivity() {

    companion object {
        //权限请求码
        private const val PERMISSION_REQUEST_CODE = 0
        //危险权限需要动态申请
        private val NEEDED_PERMISSIONS = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.READ_PHONE_STATE)

        private const val REQUEST_PHONE_STATE = 1001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_main)

        if (!checkPermission()){
            requestPermission()
        }

        btn_baidu_map.setOnClickListener { EntranceActivity.start(this) }
    }

    /**
     * 检查是否已经授予权限
     *
     * @return boolean permission
     */
    private fun checkPermission(): Boolean {
        for (permission in NEEDED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * 申请权限
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                NEEDED_PERMISSIONS, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        //判断请求码，确定当前申请的权限
        if (requestCode == REQUEST_PHONE_STATE) {
            //判断权限是否申请通过
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //success
            } else {
                Toast.makeText(this, "相关权限未通过，请去设置中允许相关权限", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

