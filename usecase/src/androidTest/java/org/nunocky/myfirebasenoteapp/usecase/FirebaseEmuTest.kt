package org.nunocky.myfirebasenoteapp.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Firebase Emulator を使ったテストの雛形
 *
 */
@RunWith(AndroidJUnit4::class)
class FirebaseEmuTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        hiltRule.inject()
        auth = FirebaseAuth.getInstance()
    }

    @After
    fun tearDown() {
        auth.signOut()
    }

    @Test
    fun test0() {
        // テストを書く
    }
}