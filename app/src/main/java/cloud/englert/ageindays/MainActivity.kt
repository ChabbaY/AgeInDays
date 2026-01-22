package cloud.englert.ageindays

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager

import cloud.englert.ageindays.data.Birthday
import cloud.englert.ageindays.databinding.ActivityMainBinding
import cloud.englert.ageindays.db.BirthdayDataSource

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var datasource: BirthdayDataSource
    var birthdayToEdit: Birthday? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences,
            false)

        datasource = BirthdayDataSource(this)
    }

    override fun onResume() {
        super.onResume()

        Log.d(LOG_TAG, "opening database")
        datasource.open()
    }

    override fun onPause() {
        super.onPause()

        Log.d(LOG_TAG, "closing database")
        datasource.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will automatically handle clicks on
        // the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(applicationContext,
                SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun saveBirthday(birthday: Birthday) {
        if (birthday.id == -1L) {
            datasource.createBirthday(birthday)
        } else {
            datasource.updateBirthday(birthday)
        }
    }

    val allBirthdays: List<Birthday>
        get() = datasource.getAllBirthdays()

    fun deleteBirthday(birthday: Birthday) {
        datasource.deleteBirthday(birthday)
    }

    companion object {
        private val LOG_TAG: String = MainActivity::class.java.simpleName
    }
}