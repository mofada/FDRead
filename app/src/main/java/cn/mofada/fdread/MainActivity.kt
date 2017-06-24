package com.example.fada.fdread

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.fada.fdread.fragment.BookShelfFragment
import com.example.fada.fdread.fragment.BookStoreFragment
import kotlinx.android.synthetic.main.layout_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navigation_bottom.setOnNavigationItemSelectedListener {
            item: MenuItem ->
            when (item.itemId) {
                R.id.menu_item_book_shelf->viewpager.currentItem=0
                R.id.menu_item_book_store->viewpager.currentItem=1
                R.id.menu_item_discuss->viewpager.currentItem=2
            }
            true
        }

        var fragments: ArrayList<Fragment> = ArrayList<Fragment>()
        fragments.add(BookShelfFragment())
        fragments.add(BookStoreFragment())
        fragments.add(BookShelfFragment())

        val adapter: FragmentPagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, fragments)
        viewpager.adapter = adapter
        viewpager.offscreenPageLimit=2
    }
}

class MyFragmentPagerAdapter(fm: FragmentManager?, var data: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = data.get(position)

    override fun getCount(): Int = data.size
}
