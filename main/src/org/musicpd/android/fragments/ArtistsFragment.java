package org.musicpd.android.fragments;

import org.a0z.mpd.Artist;
import org.a0z.mpd.Genre;
import org.a0z.mpd.Item;
import org.a0z.mpd.MPDCommand;
import org.a0z.mpd.exception.MPDServerException;

import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;

import org.musicpd.android.R;
import org.musicpd.android.library.ILibraryFragmentActivity;
import org.musicpd.android.tools.Log;
import org.musicpd.android.tools.StringResource;
import org.musicpd.android.tools.Tools;

public class ArtistsFragment extends BrowseFragment {
	private Genre genre = null;

	public ArtistsFragment() {
		super(R.string.addArtist, R.string.artistAdded, MPDCommand.MPD_SEARCH_ARTIST);
	}

	@Override
	public int getLoadingText() {
		return R.string.loadingArtists;
	}

	public ArtistsFragment init(Genre g) {
		genre = g;
		return this;
	}

	@Override
	public StringResource getTitle() {
		if (genre != null) {
			return new StringResource(genre.getName());
		} else {
			return new StringResource(R.string.artists);
		}
	}

	@Override
	public void onItemClick(AdapterView adapterView, View v, int position, long id) {
		((ILibraryFragmentActivity) getActivity()).pushLibraryFragment(new AlbumsFragment().init(genre, (Artist) items.get(position)), "album");
	}

	@Override
	protected void asyncUpdate() {
		try {
			if (genre != null) {
				items = app.oMPDAsyncHelper.oMPD.getArtists(genre);
			} else {
				items = app.oMPDAsyncHelper.oMPD.getArtists();
			}
		} catch (MPDServerException e) {
			Log.w(e);
		}
	}

	@Override
	protected String[] info(Item item) {
		return new String[] { "artist", item.getName() };
	}

	@Override
	protected void add(Item item, boolean replace, boolean play) {
		try {
			app.oMPDAsyncHelper.oMPD.add((Artist) item, replace, play);
			Tools.notifyUser(String.format(getResources().getString(irAdded), item), getActivity());
		} catch (Exception e) {
			Log.w(e);
		}
	}

	@Override
	protected void add(Item item, String playlist) {
		try {
			app.oMPDAsyncHelper.oMPD.addToPlaylist(playlist, (Artist) item);
			Tools.notifyUser(String.format(getResources().getString(irAdded), item), getActivity());
		} catch (Exception e) {
			Log.w(e);
		}
	}
}
