// Generated code from Butter Knife. Do not modify!
package com.lll.app.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.lll.app.ui.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361802, "field 'mGridView'");
    target.mGridView = finder.castView(view, 2131361802, "field 'mGridView'");
  }

  @Override public void unbind(T target) {
    target.mGridView = null;
  }
}
