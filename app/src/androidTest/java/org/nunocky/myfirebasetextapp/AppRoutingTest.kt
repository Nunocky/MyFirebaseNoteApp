package org.nunocky.myfirebasetextapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.nunocky.myfirebasetextapp.data.User
import org.nunocky.myfirebasetextapp.di.AuthenticationModule
import org.nunocky.myfirebasetextapp.domain.Authentication

class FakeAuthentication : Authentication {
    private var user: User? = null

    fun setCurrentUser(user: User?) {
        this.user = user
    }

    override fun currentUser(): User? = user
}

private val testUser = User(
    uid = "testUserId",
    displayName = "Test User",
    email = "test@example.com",
    photoUrl = "https://example.com/photo.jpg"
)

@HiltAndroidTest
@UninstallModules(AuthenticationModule::class)
@RunWith(AndroidJUnit4::class)
class AppRoutingTest {
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @BindValue
    val fakeAuth: Authentication = FakeAuthentication()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `ログイン状態だと起動直後はHome画面`() {
        (fakeAuth as FakeAuthentication).setCurrentUser(testUser)

        composeTestRule.setContent {
            AppRouting()
        }

        // "Notes"というタイトルが表示されていることを確認
        composeTestRule.onNodeWithText("Notes").assertIsDisplayed()
    }

    @Test
    fun `未ログイン状態だと起動直後はLogin画面`() {
        (fakeAuth as FakeAuthentication).setCurrentUser(null)

        composeTestRule.setContent {
            AppRouting()
        }

        // "Login"というタイトルが表示されていることを確認
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
    }

    @Test
    fun `Navigation_from_Home_to_NewItem`() {
        // ログイン状態である
        (fakeAuth as FakeAuthentication).setCurrentUser(testUser)

        composeTestRule.setContent {
            AppRouting()
        }

        // FABをクリックしてNewItem画面に遷移することを確認する
        composeTestRule.onNodeWithTag("FAB").performClick()
        composeTestRule.onNodeWithText("Create New Item").assertIsDisplayed()
    }

//    @Test
//    fun `Navigation from Home to EditItem`() {
//        // From the Home screen, trigger the onRequestEditItem callback with a valid itemId and verify that the EditItem screen is displayed with the correct itemId.
//        // TODO implement test
//    }
//
//    @Test
//    fun `Login success navigation`() {
//        // From the Login screen, trigger the onLoginSuccess callback and verify that it navigates back to the previous screen (Home).
//        // TODO implement test
//    }
//
//    @Test
//    fun `Login cancelled app termination`() {
//        // From the Login screen, trigger the onLoginCancelled callback and verify that the current activity is finished.
//        // TODO implement test
//    }
//
//    @Test
//    fun `NewItem save success navigation`() {
//        // From the NewItem screen, trigger the onSaveSuccess callback and verify that it navigates back to the previous screen (Home).
//        // TODO implement test
//    }
//
//    @Test
//    fun `NewItem edit cancelled navigation`() {
//        // From the NewItem screen, trigger the onEditCancelled callback and verify that it navigates back to the previous screen (Home).
//        // TODO implement test
//    }
//
//    @Test
//    fun `EditItem save success navigation`() {
//        // From the EditItem screen, trigger the onSaveSuccess callback and verify that it navigates back to the previous screen (Home).
//        // TODO implement test
//    }
//
//    @Test
//    fun `EditItem edit cancelled navigation`() {
//        // From the EditItem screen, trigger the onEditCancelled callback and verify that it navigates back to the previous screen (Home).
//        // TODO implement test
//    }
//
//    @Test
//    fun `ViewModel injection for Home`() {
//        // Verify that HomeViewModel is correctly injected into the HomeRoute.
//        // TODO implement test
//    }
//
//    @Test
//    fun `ViewModel injection for Login`() {
//        // Verify that LoginViewModel is correctly injected into the LoginRoute.
//        // TODO implement test
//    }
//
//    @Test
//    fun `ViewModel injection for NewItem`() {
//        // Verify that NewItemViewModel is correctly injected into the NewItemRoute.
//        // TODO implement test
//    }
//
//    @Test
//    fun `ViewModel injection for EditItem`() {
//        // Verify that EditItemViewModel is correctly injected into the EditItemRoute.
//        // TODO implement test
//    }
//
//    @Test
//    fun `EditItem navigation with empty itemId`() {
//        // Navigate to EditItem with an empty string for itemId and verify the behavior (e.g., error handling or specific UI state).
//        // TODO implement test
//    }
//
//    @Test
//    fun `EditItem navigation with null itemId  if possible `() {
//        // Although the data class defines itemId as non-nullable, test if any scenario could lead to a null-like value being passed and how it's handled. This might involve testing the `toRoute()` extension.
//        // TODO implement test
//    }
////
////    @Test
////    fun `Deep linking to Home`() {
////        // If applicable, test if deep linking directly to the Home route works as expected.
////        // TODO implement test
////    }
////
////    @Test
////    fun `Deep linking to Login`() {
////        // If applicable, test if deep linking directly to the Login route works as expected.
////        // TODO implement test
////    }
////
////    @Test
////    fun `Deep linking to NewItem`() {
////        // If applicable, test if deep linking directly to the NewItem route works as expected.
////        // TODO implement test
////    }
////
////    @Test
////    fun `Deep linking to EditItem with valid itemId`() {
////        // If applicable, test if deep linking directly to the EditItem route with a valid itemId works as expected.
////        // TODO implement test
////    }
////
////    @Test
////    fun `Deep linking to EditItem with invalid itemId`() {
////        // If applicable, test if deep linking directly to the EditItem route with an invalid or non-existent itemId is handled gracefully (e.g., error screen, navigation to Home).
////        // TODO implement test
////    }
//
//    @Test
//    fun `Back stack behavior after multiple navigations`() {
//        // Perform a sequence of navigations (e.g., Home -> NewItem -> back, Home -> Login -> back) and verify the back stack is managed correctly (e.g., popBackStack() leads to the expected screen).
//        // TODO implement test
//    }
//
//    @Test
//    fun `Back stack behavior after Login success`() {
//        // Navigate Home -> Login, then trigger onLoginSuccess. Verify that the Login screen is popped and Home is the current screen.
//        // TODO implement test
//    }
//
//    @Test
//    fun `Context for Login cancelled when not an Activity`() {
//        // Test the onLoginCancelled callback when the LocalContext.current is not an Activity (e.g., in a preview or specific test setup) to ensure no crash occurs, though app termination might not happen.
//        // TODO implement test
//    }
//
//    @Test
//    fun `Rapid sequential navigation calls`() {
//        // Trigger multiple navigation events in quick succession (e.g., from Home, rapidly call onCreateNewItem multiple times) and observe the behavior (e.g., only one navigation occurs, no crashes).
//        // TODO implement test
//    }
//
//    @Test
//    fun `Configuration changes during navigation`() {
//        // Initiate a navigation and then trigger a configuration change (e.g., screen rotation). Verify that the navigation completes correctly and the UI state is preserved as expected on the destination screen.
//        // TODO implement test
//    }
//
//    @Test
//    fun `Navigation with invalid route object`() {
//        // Attempt to navigate using an undefined or malformed serializable route object and verify that the NavHost handles this gracefully (e.g., throws an appropriate exception, navigates to a default error screen, or stays on the current screen).
//        // TODO implement test
//    }

}