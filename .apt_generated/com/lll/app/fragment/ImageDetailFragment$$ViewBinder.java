// Generated code from Butter Knife. Do not modify!
package com.lll.app.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ImageDetailFragment$$ViewBinder<T extends com.lll.app.fragment.ImageDetailFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361814, "field 'progressBar'");
    target.progressBar = finder.castView(view, 2131361814, "field 'progressBar'");
    view = finder.findRequiredView(source, 2131361813, "field 'mImageView'");
    target.mImageView = finder.castView(view, 2131361813, "field 'mImageView'");
  }

  @Override public void unbind(T target) {
    target.progressBar = null;
    target.mImageView = null;
  }
}
