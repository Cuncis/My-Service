package com.cuncisboss.myservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var mServiceBound = false
    private lateinit var mBoundService: MyBoundService
    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mServiceBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val myBinder = service as MyBoundService.MyBinder
            mBoundService = myBinder.getService
            mServiceBound = true
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start_service.setOnClickListener(this)
        btn_start_intent_service.setOnClickListener(this)
        btn_start_bound_service.setOnClickListener(this)
        btn_stop_bound_service.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start_service -> {
                val startServiceIntent = Intent(this, MyService::class.java)
                startService(startServiceIntent)
            }
            R.id.btn_start_intent_service -> {
                val startIntentService = Intent(this, MyIntentService::class.java)
                startIntentService.putExtra(MyIntentService.EXTRA_DURATION, 5000L)
                startService(startIntentService)
            }
            R.id.btn_start_bound_service -> {
                val mBoundServiceIntent = Intent(this, MyBoundService::class.java)
                bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
            }
            R.id.btn_stop_bound_service -> {
                unbindService(mServiceConnection)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mServiceBound) {
            unbindService(mServiceConnection)
        }
    }
}