package bd.com.albin.news.ui.screens.onboarding

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bd.com.albin.news.R
import bd.com.albin.news.ui.theme.NewsTheme

@Composable
fun OnboardingScreen(onAcceptClick: () -> Unit) {
    val activity = (LocalContext.current as? Activity)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.newspaper),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f),
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Welcome", style = MaterialTheme.typography.displaySmall)
            Text(
                "Thanks for joining.\n\nBy clicking accept, you agree to our terms of services.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = { activity?.finish() }) {
                    Text(text = "Cancel")
                }
                Button(onClick = onAcceptClick) {
                    Text(text = "Accept")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewOnboardingScreen() {
    NewsTheme {
        OnboardingScreen {}
    }
}