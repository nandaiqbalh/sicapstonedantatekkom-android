package com.kel022322.sicapstonedantatekkom.di

import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.login.AuthLoginRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.login.AuthLoginRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.logout.AuthLogoutRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.auth.logout.AuthLogoutRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast.BroadcastRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.broadcast.BroadcastRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen.DosenRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.dosen.DosenRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.expo.ExpoRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.expo.ExpoRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.file.FileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.file.FileRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok.KelompokSayaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.kelompok.KelompokSayaRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.mahasiswa.MahasiswaRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.mahasiswa.MahasiswaRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile.ProfileRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.profile.ProfileRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangproposal.SidangProposalRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangproposal.SidangProposalRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangta.SidangTARemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.sidangta.SidangTARemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.siklus.SiklusRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.siklus.SiklusRemoteDataSourceImpl
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.topik.TopikRemoteDataSource
import com.kel022322.sicapstonedantatekkom.data.remote.datasource.topik.TopikRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

	// auth
	@Binds
	abstract fun provideAuthRemoteDataSource(authRemoteDataSourceImpl: AuthLoginRemoteDataSourceImpl): AuthLoginRemoteDataSource

	@Binds
	abstract fun provideAuthLogoutRemoteDataSource(authLogoutRemoteDataSourceImpl: AuthLogoutRemoteDataSourceImpl) : AuthLogoutRemoteDataSource

	// broadcast
	@Binds
	abstract fun provideBroadcastRemoteDataSource(broadcastRemoteDataSourceImpl: BroadcastRemoteDataSourceImpl): BroadcastRemoteDataSource

	@Binds
	abstract fun provideProfileRemoteDataSource(profileRemoteDataSourceImpl: ProfileRemoteDataSourceImpl): ProfileRemoteDataSource

	@Binds
	abstract fun provideKelompokSayaRemoteDataSource(kelompokSayaRemoteDataSourceImpl: KelompokSayaRemoteDataSourceImpl): KelompokSayaRemoteDataSource

	@Binds
	abstract fun provideFileRemoteDataSource(fileRemoteDataSourceImpl: FileRemoteDataSourceImpl): FileRemoteDataSource

	@Binds
	abstract fun provideDosenRemoteDataSource(dosenRemoteDataSourceImpl: DosenRemoteDataSourceImpl): DosenRemoteDataSource

	@Binds
	abstract fun provideMahasiswaRemoteDataSource(mahasiswaRemoteDataSourceImpl: MahasiswaRemoteDataSourceImpl): MahasiswaRemoteDataSource

	@Binds
	abstract fun provideTopikRemoteDataSource(topikRemoteDataSourceImpl: TopikRemoteDataSourceImpl): TopikRemoteDataSource


	@Binds
	abstract fun provideSiklusRemoteDataSource(siklusRemoteDataSourceImpl: SiklusRemoteDataSourceImpl): SiklusRemoteDataSource

	@Binds
	abstract fun provideSidangProposalRemoteDataSource(sidangProposalDataSourceImpl: SidangProposalRemoteDataSourceImpl): SidangProposalRemoteDataSource

	@Binds
	abstract fun provideExpoRemoteDataSource(expoRemoteDataSourceImpl: ExpoRemoteDataSourceImpl): ExpoRemoteDataSource

	@Binds
	abstract fun provideSidangTARemoteDataSource(sidangTARemoteDataSourceImpl: SidangTARemoteDataSourceImpl): SidangTARemoteDataSource

}