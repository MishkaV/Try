package com.example.homework1

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    companion object {
        const val spanCountVertical = 3
        const val spanCountHorizontal = 4
    }
    var digitsList: ListNumbers = ListNumbers()
    var adapter: NumAdapter = NumAdapter(digitsList)
    lateinit var numList: RecyclerView
    val mainFrag = MainFragment()
    var checkFragment: Int = 0
    var clickedNum: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkFragment = 0
        numList = findViewById(R.id.recyclerView)
        numList.layoutManager = GridLayoutManager(baseContext, spanCountVertical, RecyclerView.VERTICAL, false)
        digitsList.init()
        numList.adapter = adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putStringArrayList("KEY", adapter.getDigits())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        numList = findViewById(R.id.recyclerView)
        if (checkFragment == 0) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                numList.layoutManager =
                    GridLayoutManager(baseContext, spanCountVertical, RecyclerView.VERTICAL, false)
            else
                numList.layoutManager =
                    GridLayoutManager(baseContext, spanCountHorizontal, RecyclerView.VERTICAL, false)
            savedInstanceState?.getStringArrayList("KEY")?.let { digitsList.setArray(it) }
            adapter = NumAdapter(digitsList)
            numList.adapter = adapter
        } else {
            val bigNum = BigNumFragment.newInstance(clickedNum)
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_fragment, bigNum)
                .commit()
        }
    }

    fun addDigit(view: View) {
        adapter.addDigit()
    }

    fun check(view: View) {
        clickedNum = adapter.itemClick(numList, view)
        checkFragment = 1
        val bigNum = BigNumFragment.newInstance(clickedNum)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, bigNum)
            .commit()
    }

    fun back(view: View) {
        checkFragment = 0
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_fragment, mainFrag)
            .commit()
    }
}
