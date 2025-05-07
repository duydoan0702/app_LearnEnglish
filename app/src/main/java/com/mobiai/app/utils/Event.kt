package com.mobiai.app.utils

import com.mobiai.base.basecode.ultility.BaseEvent
import com.mobiai.base.basecode.ultility.RxBus
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
class IsRecordHaveText(val textRecord:String) : BaseEvent()

fun listenEvent(
    onSuccess: (e: BaseEvent) -> Unit,
    onError: (th: Throwable) -> Unit = {}
): Disposable {
    return RxBus.listen(BaseEvent::class.java)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            onSuccess(it)
        }, {
            onError(it)
        })
}

