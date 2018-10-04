package com.example.database;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Элемет справочника "Контент". В данном проекте не используется 
 * 
 * @author legioner
 *
 */
public class Content extends BaseDeletable {
	
	@Column(name = "title", columnDefinition = "VARCHAR")
	private String title;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "content_kind")
	private EnumContentKind contentKind;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "performer_id")
	private Performer	performer;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "music_author_id")
	private MusicAuthor	musicAuthor;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "text_author_id")
	private TextAuthor	textAuthor;
	
	@Column(insertable = false, name = "performer_id")
	private Long performerId;

	@Column(insertable = false, name = "text_author_id")
	private Long textAuthorId;

	@Column(insertable = false, name = "music_author_id")
	private Long musicAuthorId;
	
	public Content() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EnumContentKind getContentKind() {
		return contentKind;
	}

	public void setContentKind(EnumContentKind contentKind) {
		this.contentKind = contentKind;
	}

	public Performer getPerformer() {
		return performer;
	}

	public void setPerformer(Performer performer) {
		this.performer = performer;
	}

	public MusicAuthor getMusicAuthor() {
		return musicAuthor;
	}

	public void setMusicAuthor(MusicAuthor musicAuthor) {
		this.musicAuthor = musicAuthor;
	}

	public TextAuthor getTextAuthor() {
		return textAuthor;
	}

	public void setTextAuthor(TextAuthor textAuthor) {
		this.textAuthor = textAuthor;
	}

	public Long getPerformerId() {
		return performerId;
	}

	public void setPerformerId(Long performerId) {
		this.performerId = performerId;
	}

	public Long getTextAuthorId() {
		return textAuthorId;
	}

	public void setTextAuthorId(Long textAuthorId) {
		this.textAuthorId = textAuthorId;
	}

	public Long getMusicAuthorId() {
		return musicAuthorId;
	}

	public void setMusicAuthorId(Long musicAuthorId) {
		this.musicAuthorId = musicAuthorId;
	}
	
	
}
