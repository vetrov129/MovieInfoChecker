package hi.dude.movieinfochecker.view.personcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import hi.dude.movieinfochecker.R
import hi.dude.movieinfochecker.application.App
import kotlinx.android.synthetic.main.activity_person_card.*
import javax.inject.Inject

class PersonCardActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: PersonCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_card)
        supportActionBar?.hide()
        window.statusBarColor = resources.getColor(R.color.colorAutoDark)

        (application as App).component.inject(this)
        val id = intent.getStringExtra("id")
        val views = TextViews(tvPersonName, tvPersonRole, tvPersonBirthDate, tvPersonSummary)

        viewModel.loadPersonInfo(id ?: "")
        viewModel.bindGeneral(rvKnownFor, this)

        viewModel.personInfo.observe(this) { viewModel.updateData(views, ivPersonPhoto) }
    }

    data class TextViews(
        val name: TextView,
        val role: TextView,
        val birthDate: TextView,
        val summary: TextView,
    )
}