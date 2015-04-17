package com.guoyonghui.weatherforecast;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.guoyonghui.weatherforecast.webservice.WeatherWebService;

public class StartUpGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8706710980333491840L;
	
	private static final String TITLE = "Loading";
	
	/**
	 * loading动画图片
	 */
	private ImageIcon mLoadingIcon = new ImageIcon("img/loading.gif");
	
	/**
	 * loading动画图片标签
	 */
	private JLabel mLoadingLabel = new JLabel(mLoadingIcon);
	
	/**
	 * 省市级联数据
	 */
	private Map<String, Vector<String>> data = new HashMap<String, Vector<String>>();
	
	public StartUpGUI() {
		initializeFrame();
		addComponent();
		initializeData();
		startWeatherForecastGUI();
	}
	
	/***
	 * 初始化当前JFrame
	 */
	private void initializeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, mLoadingIcon.getIconWidth(), mLoadingIcon.getIconHeight());
		setTitle(TITLE);
		setLayout(new BorderLayout());
		setVisible(true);
		setResizable(false);
	}
	
	/***
	 * 添加组件
	 */
	private void addComponent() {
		add(mLoadingLabel, BorderLayout.CENTER);
	}
	
	/***
	 * 初始化省市级联数据
	 */
	private void initializeData() {
		Vector<String> provinceList = WeatherWebService.getSupportProvince();
		for(String province: provinceList) {
			Vector<String> cityList = WeatherWebService.getSupportCity(province);
			data.put(province, cityList);
		}
	}
	
	/***
	 * 启动天气预报GUI
	 */
	private void startWeatherForecastGUI() {
		dispose();
		new WeatherForecastGUI(data, (int)this.getLocation().getX(), (int)this.getLocation().getY());
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		new StartUpGUI();
	}
	
} 