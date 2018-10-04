package com.example.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.example.dao.EntityDao;
import com.example.database.Dictonary;
import com.example.database.MusicAuthor;
import com.example.database.Performer;
import com.example.database.TextAuthor;

/**
 * Это основной (и единственный) managed bean для jsf страниц
 * Реализован только базовый функционал 3-х справочников 
 * (это все же больше "посмотреть", чем реальный выбор для реализации UI) 
 * 
 * @author legioner
 *
 */
@SessionScope
@ManagedBean(name = "menuView")
@Component
public class MenuView {
	
	//Вид справочника, с которым мы сейчас работаем
	private enum PageType {
		performer,
		musicauthor,
		textauthor
	}
	
	//BaseDao, инжектится в конструкторе
	private EntityDao entityDao;
	
	@Autowired
	public MenuView(EntityDao dao) {
		this.entityDao = dao; 
	}
	
	//Текущий тип страницы
	private PageType actualPageType = null;
	
	//Поля бина для работы со справочниками - исполнитель
	private List<Performer> performers = null;
	private Performer selectedPerformer = null;
	private Boolean disableChnagePerformerButton = true; 

	//Поля бина для работы со справочниками - автор музыки
	private List<MusicAuthor> musicAuthors = null;
	private MusicAuthor selectedMusicAuthor = null;
	private Boolean disableChnageMusicAuthorButton = true; 

	//Поля бина для работы со справочниками - автор текста
	private List<TextAuthor> textAuthors = null;
	private TextAuthor selectedTextAuthor = null;
	private Boolean disableChnageTextAuthorButton = true;

	//Наименование для нового элемента для любого справочника
	private String newDictonaryTitle;
	
	//Обновить списки после создания бина. Для удобства - правльно эту логику стоило бы размещать в actualTextAuthor
	// и аналогичных - чтобы грузить именно список для той страницы, с которой работаем
    @PostConstruct
    public void init() {
    	updateLists(null);
    }
    
    //Перестроить списки (анпример, после добавления или удаления данных)
    private void updateLists(PageType actualPageType) {
    	if (actualPageType == null) {
			musicAuthors = entityDao.list(MusicAuthor.class, null, null, 0, 10);
			performers = entityDao.list(Performer.class, null, null, 0, 10);
			textAuthors = entityDao.list(TextAuthor.class, null, null, 0, 10);
			return;
    	}
    	switch (actualPageType) {
		case musicauthor:
			musicAuthors = entityDao.list(MusicAuthor.class, null, null, 0, 10);
			break;
		case performer:
			performers = entityDao.list(Performer.class, null, null, 0, 10);
			break;
		case textauthor:
			textAuthors = entityDao.list(TextAuthor.class, null, null, 0, 10);
			break;
    	}
    }

    /*
     *  Геттеры-сеттеры
     */
	public Performer getSelectedPerformer() {
		return selectedPerformer;
	}

	public void setSelectedPerformer(Performer selectedPerformer) {
		this.selectedPerformer = selectedPerformer;
	}

	public List<Performer> getPerformers() {
		return performers;
	}
	
	public Boolean getDisableChnagePerformerButton() {
		return disableChnagePerformerButton;
	}

	public MusicAuthor getSelectedMusicAuthor() {
		return selectedMusicAuthor;
	}

	public void setSelectedMusicAuthor(MusicAuthor selectedMusicAuthor) {
		this.selectedMusicAuthor = selectedMusicAuthor;
	}

	public String getNewDictonaryTitle() {
		return newDictonaryTitle;
	}

	public void setNewDictonaryTitle(String newDictonaryTitle) {
		this.newDictonaryTitle = newDictonaryTitle;
	}

	public List<MusicAuthor> getMusicAuthors() {
		return musicAuthors;
	}

	public Boolean getDisableChnageMusicAuthorButton() {
		return disableChnageMusicAuthorButton;
	}

	public TextAuthor getSelectedTextAuthor() {
		return selectedTextAuthor;
	}

	public void setSelectedTextAuthor(TextAuthor selectedTextAuthor) {
		this.selectedTextAuthor = selectedTextAuthor;
	}

	public List<TextAuthor> getTextAuthors() {
		return textAuthors;
	}

	public Boolean getDisableChnageTextAuthorButton() {
		return disableChnageTextAuthorButton;
	}
    /*
     *  Геттеры-сеттеры закончились
     */

	
	//Методы для установки в бине параметров текущего справочника (с которым работаем) 
	public String actualTextAuthor() {
		actualPageType = PageType.textauthor;
		System.out.println("now we work with text authors");
		return "/textAuthors.xhtml?faces-redirect=true";
	}

	public String actualMusicAuthor() {
		actualPageType = PageType.musicauthor;
		System.out.println("now we work with music authors");
		return "/musicAuthors.xhtml?faces-redirect=true";
	}

	public String actualPerformer() {
		actualPageType = PageType.performer;
		System.out.println("now we work with performer");
		return "/performers.xhtml?faces-redirect=true";
	}
	
	//Сохрнаить новый элемент справочника
	public void save() {
		System.out.println("save");
		Dictonary entity = null;
		switch (actualPageType) {
		case musicauthor:
			entity = new MusicAuthor();
			break;
		case textauthor:
			entity = new TextAuthor();
			break;
		case performer:
			entity = new Performer();
			break;
		}
		entity.setTitle(newDictonaryTitle);
		entityDao.save(entity);
		newDictonaryTitle = null;
		updateLists(actualPageType);
	}

	//Обновить элемент справочника
	public void update() {
		switch (actualPageType) {
		case musicauthor:
			entityDao.update(selectedMusicAuthor);
			break;
		case textauthor:
			entityDao.update(selectedTextAuthor);
			break;
		case performer:
			entityDao.update(selectedPerformer);
			break;
		}
		updateLists(actualPageType);
	}
	
	//Удалить элемент справочника
	public void delete() {
		System.out.println("deleteing");
		switch (actualPageType) {
		case musicauthor:
			entityDao.delete(MusicAuthor.class, selectedMusicAuthor);
			selectedMusicAuthor = null;
			break;
		case textauthor:
			entityDao.delete(TextAuthor.class, selectedTextAuthor);
			selectedTextAuthor = null;
			break;
		case performer:
			entityDao.delete(Performer.class, selectedPerformer);
			selectedPerformer = null;
			break;
		}
		updateLists(actualPageType);
		updateButtons();
	}

	//Методы - при выборе строки справочника
	//Тут осталось загадкой - возможно ли в принципе иметь один метод на все справчоники 
	//(можно попробовать selected<Entity> сделать базового типа - но как-то руки не дошли) 
	public void onSelect(TextAuthor textAuthor) {
		System.out.println("select textAuthor " + textAuthor);
		selectedTextAuthor = textAuthor;
		updateButtons();
	}

	public void onSelect(Performer performer) {
		System.out.println("select performer " + performer);
		selectedPerformer = performer;
		updateButtons();
	}

	public void onSelect(MusicAuthor musicAuthor) {
		System.out.println("select musicAuthor " + musicAuthor);
		selectedMusicAuthor = musicAuthor;
		updateButtons();
	}

	//Методы - при снятии выделения со строки справочника
	public void onDeselect(TextAuthor textAuthor) {
		System.out.println("deselect");
		selectedTextAuthor = null;
		updateButtons();
	}

	public void onDeselect(Performer performer) {
		System.out.println("deselect");
		selectedPerformer = null;
		updateButtons();
	}
	
	public void onDeselect(MusicAuthor musicAuthor) {
		System.out.println("deselect");
		selectedMusicAuthor = null;
		updateButtons();
	}

	//Обновить доступность кнопок после выбора/снятия выбора со строки 	
	private void updateButtons() {
		disableChnageTextAuthorButton = selectedTextAuthor  == null;
		disableChnagePerformerButton = selectedPerformer  == null;
		disableChnageMusicAuthorButton = selectedMusicAuthor == null;
	}

	
	
}
