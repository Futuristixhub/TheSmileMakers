package com.smilemakers.ui.dashBoard.profile

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.smilemakers.R
import com.smilemakers.databinding.ActivityEditProfileBinding
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class EditProfileActivity : AppCompatActivity() , KodeinAware {

    override val kodein by kodein()
    var binding: ActivityEditProfileBinding? = null
    private val factory: ProfileViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        val  viewModel =
            ViewModelProviders.of(this, factory).get(ProfileVM::class.java)
        binding?.vm = viewModel


        val bar: ActionBar? = supportActionBar
        if (bar != null) {
            val tv = TextView(applicationContext)
            val lp: ActionBar.LayoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,  // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT
            ) // Height of TextView
            tv.layoutParams = lp
            tv.setText(getString(R.string.edit_profile))
            tv.setTextColor(Color.WHITE)

            val typedValue = TypedValue()
            resources.getValue(R.dimen.actionBar_text, typedValue, true)
            val myFloatValue = typedValue.float

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, myFloatValue)
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            bar.setDisplayHomeAsUpEnabled(true)
            bar.setCustomView(tv)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
