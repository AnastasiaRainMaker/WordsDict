//package com.dmitry.wordsdict;
//
///**
// * Created by dmitry on 5/28/17.
// */
//
//
//import com.dmitry.wordsdict.main.views.MainView;
//import com.dmitry.wordsdict.main.interactors.SaveWordInteractorImpl;
//import com.dmitry.wordsdict.main.presenters.TranslatePresenter;
//import com.dmitry.wordsdict.main.presenters.TranslatePresenterImpl;
//import com.dmitry.wordsdict.main.interactors.TranslateWordInteractor;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertNull;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@RunWith(MockitoJUnitRunner.class)
//public class TranslatePresenterTest {
//
//    @Mock
//    MainView view;
//    @Mock
//    TranslateWordInteractor interactor;
//    @Mock
//    SaveWordInteractorImpl saveWordInteractor;
//
//    private TranslatePresenter presenter;
//
//    @Before
//    public void setUp() throws Exception {
//        presenter = new TranslatePresenterImpl(view, interactor, saveWordInteractor);
//    }
//
//    @Test
//    public void checkIfShowsProgressOnResume() {
//        presenter.onTranslateButtonClicked("e");
//        verify(view, times(1)).showProgress();
//    }
//
//    @Test
//    public void checkIfShowsMessageOnItemClick() {
//        presenter.onTranslateButtonClicked("wordTest");
//        verify(view, times(1)).showTranslation(anyString());
//    }
//
////    @Test
////    public void checkIfRightMessageIsDisplayed() {
////        ArgumentCaptor<String> captor = forClass(String.class);
////        presenter.onTranslateButtonClicked("wordTest");
////        verify(view, times(1)).showTranslation(captor.capture());
////        assertThat(captor.getValue(), is("wordTest translated"));
////    }
//
////    @Test
////    public void checkIfViewIsReleasedOnDestroy() {
////        presenter.onDestroy();
//////        assertNull(presenter.);
////    }
//
////    @Test
////    public void checkIfItemsArePassedToView() {
////        List<String> items = Arrays.asList("Model", "View", "Controller");
////        presenter(items);
////        verify(view, times(1)).setItems(items);
////        verify(view, times(1)).hideProgress();
////    }
//
//}
