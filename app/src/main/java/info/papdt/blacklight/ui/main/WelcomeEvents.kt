package info.papdt.blacklight.ui.main

import info.papdt.blacklight.support.helper.Event

/**
 * Created by peter on 7/2/16.
 */
class FinishEvent : Event
class NextButtonClickEvent : Event
class NextPageEvent : Event
data class PageChangeEvent(var page: Int) : Event
class DisableNextButtonEvent : Event
class EnableNextButtonEvent : Event
data class LoadUrlEvent(var url: String) : Event
data class RedirectEvent(var url: String) : Event
class ShowLoginFailedMessageEvent : Event
class PageOverflowEvent : Event
data class WelcomeUserEvent(var username: String) : Event