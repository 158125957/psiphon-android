package com.psiphon3.psicash;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.psiphon3.psicash.util.LceStatus;

public interface PsiCashResult {
    @AutoValue
    abstract class GetPsiCash implements PsiCashResult {
        @NonNull
        static GetPsiCash success(PsiCashModel.PsiCash model) {
            return new AutoValue_PsiCashResult_GetPsiCash(LceStatus.SUCCESS, model, null);
        }

        @NonNull
        static GetPsiCash failure(Throwable error) {
            return new AutoValue_PsiCashResult_GetPsiCash(LceStatus.FAILURE, null, error);
        }

        @NonNull
        static GetPsiCash inFlight() {
            return new AutoValue_PsiCashResult_GetPsiCash(LceStatus.IN_FLIGHT, null, null);
        }

        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract PsiCashModel.PsiCash model();

        @Nullable
        abstract Throwable error();
    }

    @AutoValue
    abstract class ExpiringPurchase implements PsiCashResult {
        @NonNull
        static ExpiringPurchase success(PsiCashModel.ExpiringPurchase model) {
            return new AutoValue_PsiCashResult_ExpiringPurchase(LceStatus.SUCCESS, model, null);
        }

        @NonNull
        static ExpiringPurchase failure(Throwable error) {
            return new AutoValue_PsiCashResult_ExpiringPurchase(LceStatus.FAILURE, null, error);
        }

        @NonNull
        static ExpiringPurchase inFlight() {
            return new AutoValue_PsiCashResult_ExpiringPurchase(LceStatus.IN_FLIGHT, null, null);
        }

        @NonNull
        abstract LceStatus status();

        @Nullable
        abstract PsiCashModel.ExpiringPurchase model();

        @Nullable
        abstract Throwable error();
    }

    @AutoValue
    abstract class ClearErrorState implements PsiCashResult {
        @NonNull
        static ClearErrorState success() {
            return new AutoValue_PsiCashResult_ClearErrorState();
        }
    }

    @AutoValue
    abstract class Video implements PsiCashResult {

        enum VideoStatus {LOADING, LOADED, OPENED, FAILURE, FINISHED}

        @NonNull
        static Video loaded(PsiCashModel.VideoReady model) {
            return new AutoValue_PsiCashResult_Video(VideoStatus.LOADED, model, null);
        }

        @NonNull
        static Video loading() {
            return new AutoValue_PsiCashResult_Video(VideoStatus.LOADING, null, null);
        }

        @NonNull
        static Video opened() {
            return new AutoValue_PsiCashResult_Video(VideoStatus.OPENED, null, null);
        }

        @NonNull
        static Video failure(Throwable error) {
            return new AutoValue_PsiCashResult_Video(VideoStatus.FAILURE, null, error);
        }

        @NonNull
        static Video finished() {
            return new AutoValue_PsiCashResult_Video(VideoStatus.FINISHED, null, null);
        }

        @NonNull
        abstract VideoStatus status();

        @Nullable
        abstract PsiCashModel.VideoReady model();

        @Nullable
        abstract Throwable error();
    }

    @AutoValue
    abstract class Reward implements PsiCashResult {
        @NonNull
        static Reward success(PsiCashModel.Reward model) {
            return new AutoValue_PsiCashResult_Reward(model);
        }

        @Nullable
        abstract PsiCashModel.Reward model();
    }

}
