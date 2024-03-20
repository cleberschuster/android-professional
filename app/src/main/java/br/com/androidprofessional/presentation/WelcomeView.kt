package br.com.androidprofessional.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.androidprofessional.R
import br.com.androidprofessional.ui.theme.ArsenalButtonRow
import br.com.androidprofessional.ui.theme.ArsenalIconImage
import br.com.androidprofessional.ui.theme.ArsenalTheme
import br.com.androidprofessional.ui.theme.ArsenalThemeExtended


@Composable
fun WelcomeView(context: Context) {
    val action = { Toast.makeText(context, "Olha isso!", Toast.LENGTH_SHORT).show() }
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {

            Card(
                modifier = Modifier
                    .size((LocalConfiguration.current.screenWidthDp.dp)/3),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(10),
            ) {
                // Column
                Text(
                    text = "Compose Arsenal",
                    style = TextStyle(fontSize = 16.sp, )
                )
            }


            Text(
                text = "Seja bem Vindo a tropa!",
                style = ArsenalThemeExtended.typography.body1,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            ArsenalIconImage(R.drawable.ic_verified) // Box
        }

        ArsenalButtonRow( // Row
            modifier = Modifier.widthIn(min = 100.dp, max = 120.dp),
            positiveAction = action,
        positiveTextId = R.string.btn_row_confirm,
            neutralAction = action,
            neutralTextId = R.string.btn_row_maybe,
            negativeAction = action,
            negativeTextId = R.string.btn_row_cancel,
        )
    }
}

@Preview
@Composable
fun WelcomeViewLightPreview() {
    ArsenalTheme(useDarkTheme = false) {
        WelcomeView(LocalContext.current)
    }
}

@Preview
@Composable
fun WelcomeViewDarkPreview() {
    ArsenalTheme(useDarkTheme = true) {
        WelcomeView(LocalContext.current)
    }
}