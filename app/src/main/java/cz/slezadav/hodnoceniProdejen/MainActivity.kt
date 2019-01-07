package cz.slezadav.hodnoceniProdejen

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import cz.slezadav.hodnoceniProdejen.Util.PermissionManager
import cz.slezadav.hodnoceniProdejen.databinding.ActivityMainBinding
import cz.slezadav.hodnoceniProdejen.model.Evaluation

class MainActivity : AppCompatActivity() {

    lateinit var ui: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = DataBindingUtil.setContentView(this, R.layout.activity_main)

        ui.done.setOnClickListener {
            goToForm()
        }

        ui.inputInspector.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if ((event?.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                goToForm()
                return@OnKeyListener true
            }
            false
        })
        if (!PermissionManager.hasStoragePermission(this)) {
            PermissionManager.requestStoragePermission(this)
        }
    }


    private fun goToForm(){
        if (!PermissionManager.hasStoragePermission(this)) {
            PermissionManager.requestStoragePermission(this)
            return
        }
        Evaluation.reset(this@MainActivity)
        Evaluation.inspectorName = ui.inputInspector.text.toString()
        Evaluation.storeName = ui.inputStore.text.toString()
        startActivity(Intent(this,FormListActivity::class.java))
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!PermissionManager.obtainedStoragePermission(requestCode, grantResults)) {
            finish()
        }
    }
}