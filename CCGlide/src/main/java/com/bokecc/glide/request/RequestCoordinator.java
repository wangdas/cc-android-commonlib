package com.bokecc.glide.request;

import com.bokecc.glide.request.target.Target;

/**
 * An interface for coordinating multiple requests with the same {@link
 * Target}.
 */
public interface RequestCoordinator {

  /**
   * Returns true if the {@link Request} can display a loaded bitmap.
   *
   * @param request The {@link Request} requesting permission to display a bitmap.
   */
  boolean canSetImage(Request request);

  /**
   * Returns true if the {@link Request} can display a placeholder.
   *
   * @param request The {@link Request} requesting permission to display a placeholder.
   */
  boolean canNotifyStatusChanged(Request request);

  /**
   * Returns {@code true} if the {@link Request} can clear the
   * {@link Target}.
   */
  boolean canNotifyCleared(Request request);

  /**
   * Returns true if any coordinated {@link Request} has successfully completed.
   *
   * @see Request#isComplete()
   */
  boolean isAnyResourceSet();

  /**
   * Must be called when a {@link Request} coordinated by this object completes successfully.
   */
  void onRequestSuccess(Request request);

  /** Must be called when a {@link Request} coordinated by this object fails. */
  void onRequestFailed(Request request);
}
