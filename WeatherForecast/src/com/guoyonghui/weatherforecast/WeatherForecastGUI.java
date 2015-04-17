package com.guoyonghui.weatherforecast;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.guoyonghui.weatherforecast.webservice.WeatherWebService;

public class WeatherForecastGUI extends JFrame implements ItemListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5316382224121735397L;
	
	private static final String TITLE = "Weather WebService";
	
	/**
	 * 省市级联数据
	 */
	private Map<String, Vector<String>> data = new HashMap<String, Vector<String>>();
	
	/**
	 * 省列表
	 */
	private JComboBox<String> mProvinceComboBox = new JComboBox<String>();
	
	/**
	 * 市列表
	 */
	private JComboBox<String> mCityComboBox = new JComboBox<String>();
	
	/**
	 * 天气信息文字区域
	 */
	private JTextArea mWeatherInfoTextArea = new JTextArea(25, 40);
	
	/**
	 * 天气信息滚动Pane
	 */
	private JScrollPane mWeatherInfoScrollPane = new JScrollPane();
	
	public WeatherForecastGUI(Map<String, Vector<String>> data, int x, int y) {
		this.data = data;
		initializeFrame(x, y);
		addComponent();
		addListener();
	}
	
	/***
	 * 根据StartUpGUI关闭时的位置初始化当前JFrame
	 * @param x StartUpGUI关闭时的横坐标
	 * @param y StartUpGUI关闭时的纵坐标
	 */
	private void initializeFrame(int x, int y) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, 400, 600);
		setTitle(TITLE);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		setVisible(true);
		setResizable(false);

		mProvinceComboBox.setModel(createComboBoxModel(data.keySet().toArray()));
		mCityComboBox.setModel(createComboBoxModel(data.get(data.keySet().toArray()[0]).toArray()));
		mWeatherInfoTextArea.setLineWrap(true);
		mWeatherInfoTextArea.setWrapStyleWord(true);
		mWeatherInfoTextArea.setEditable(false);
		mWeatherInfoScrollPane = new JScrollPane(mWeatherInfoTextArea);
	}
	
	/***
	 * 添加组件
	 */
	private void addComponent() {
		add(mProvinceComboBox);
		add(mCityComboBox);
		add(mWeatherInfoScrollPane);
		
		setWeatherInfo(mCityComboBox.getItemAt(0));
	}
	
	/***
	 * 添加监听器
	 */
	private void addListener() {
		mProvinceComboBox.addItemListener(this);
		mCityComboBox.addItemListener(this);
	}
	
	/***
	 * 根据传入的数组创建ComboBox数据模型
	 * @param model 数据数组
	 * @return ComboBox数据模型
	 */
	private DefaultComboBoxModel<String> createComboBoxModel(Object[] model) {
		String[] items = new String[model.length];
		for(int i = 0; i < model.length; i++) {
			items[i] = (String)model[i];
		}
		return new DefaultComboBoxModel<String>(items);
	}
	
	/***
	 * 根据城市设置天气信息
	 * @param cityName 城市
	 */
	private void setWeatherInfo(final String cityName) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String city = cityName.substring(cityName.indexOf("(") + 1, cityName.indexOf(")"));
				Vector<String> weatherInfo = WeatherWebService.getWeatherbyCityNamePro(city);
				mWeatherInfoTextArea.setText("");
				for(String info: weatherInfo) {
					mWeatherInfoTextArea.append(info + "\n");
				}				
				mWeatherInfoTextArea.setSelectionStart(0);
				mWeatherInfoTextArea.setSelectionEnd(0);
			}
		}).start();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource().equals(mProvinceComboBox)) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				mCityComboBox.setModel(createComboBoxModel(data.get(e.getItem()).toArray()));
				setWeatherInfo(mCityComboBox.getItemAt(0));
			}
		} else if(e.getSource().equals(mCityComboBox)) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				String cityName = (String)e.getItem();
				setWeatherInfo(cityName);
			}
		}
	}
	
}
