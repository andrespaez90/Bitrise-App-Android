package com.example.lupita.di.modules

import androidx.lifecycle.ViewModel
import com.bitrise.app.viewModels.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ViewModelModule {

    @Binds
    abstract fun bindMainViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

}
