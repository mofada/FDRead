package cn.mofada.fdread

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.MenuItem
import cn.mofada.fdread.base.Constant
import cn.mofada.fdread.dialog.SetLoadingDialog
import cn.mofada.fdread.fragment.BookDiscussFragment
import cn.mofada.fdread.fragment.BookShelfFragment
import cn.mofada.fdread.fragment.BookStoreFragment
import cn.mofada.fdread.utils.ToastUtils
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_head.*
import kotlinx.android.synthetic.main.layout_main.*


class MainActivity : AppCompatActivity() {
    val bookShelfFragment: BookShelfFragment = BookShelfFragment()
    val bookStoreFragment: BookStoreFragment = BookStoreFragment()
    val bookDiscussFragment: BookDiscussFragment = BookDiscussFragment()
    var dialogSet: SetLoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        dialogSet = SetLoadingDialog(this, R.style.loadingDialog)

        navigation_bottom.setOnNavigationItemSelectedListener {
            item: MenuItem ->
            when (item.itemId) {
                R.id.menu_item_book_shelf -> viewpager.currentItem = 0
                R.id.menu_item_book_store -> viewpager.currentItem = 1
                R.id.menu_item_discuss -> viewpager.currentItem = 2
            }
            true
        }

        val fragments: ArrayList<Fragment> = ArrayList<Fragment>()
        fragments.add(bookShelfFragment)
        fragments.add(bookStoreFragment)
        fragments.add(bookDiscussFragment)

        viewpager.offscreenPageLimit = 2
        viewpager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = fragments[position]

            override fun getCount(): Int = fragments.size
        }

        navigationView.setNavigationItemSelectedListener {
            item: MenuItem ->

            when (item.itemId) {
                R.id.menu_item_user -> startUse()
                R.id.menu_item_loading -> showLoadingSet()
            }
            true
        }

        registerReceiver()
    }

    fun showLoadingSet() {
        drawLayout.closeDrawers()
        dialogSet!!.show()
    }

    fun startUse() {
        UserActivity.startActivity(this)
        drawLayout.closeDrawers()
    }

    //记录用户首次点击返回键的时间
    private var firstTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - firstTime > 2000) {
                ToastUtils.showToast(this@MainActivity, "再按一次退出程序")
                firstTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        fun startActivity(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }

    fun registerReceiver() {
        val intentFilter: IntentFilter = IntentFilter()
        intentFilter.addAction(Constant.ACTION_LOGININ)
        intentFilter.addAction(Constant.ACTION_LOGINOUT)
        intentFilter.addAction(Constant.ACTION_BOOKADD)
        registerReceiver(mBroadcastReceiver, intentFilter)
    }

    var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Constant.ACTION_LOGININ -> loginIn(intent)
                Constant.ACTION_LOGINOUT -> loginOut()
                Constant.ACTION_BOOKADD -> addBook()
            }
        }
    }

    fun loginIn(intent: Intent) {
        if (TextUtils.isEmpty(intent.getStringExtra(Constant.USER_ICONURL))) return
        bookShelfFragment.loginIn(intent)
        bookDiscussFragment.loginIn(intent)
        bookStoreFragment.loginIn(intent)
        Glide.with(this).load(intent.getStringExtra(Constant.USER_ICONURL)).into(user_icon)
        user_name.text = intent.getStringExtra(Constant.USER_NAME)
    }

    fun loginOut() {
        bookShelfFragment.loginOut()
        bookDiscussFragment.loginOut()
        bookStoreFragment.loginOut()
        Glide.with(this).load(R.mipmap.png_user).into(user_icon)
        user_name.text = "name"
    }

    fun addBook() {
        bookShelfFragment.getBook()
    }

    override fun onDestroy() {
        unregisterReceiver(mBroadcastReceiver)
        super.onDestroy()
    }
}
