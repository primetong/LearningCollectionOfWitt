﻿/**********************************************************************
 *
 *	@file		RandomRecordGenerator.cpp
 *  Created by Witt
 *
 *********************************************************************/

#include <algorithm>
#include <unordered_map>
#include <random>
#include <string>
#include <iostream>
#include <iomanip>
#include <vector>
#include <sstream>

typedef unsigned int uint;
static std::random_device id;
static std::mt19937 engine(id());

std::string random_number(uint min, uint max) 
{
	static std::uniform_int_distribution<uint> GENERATOR(0U, ~0U);
	std::ostringstream oss;
	oss << (min + GENERATOR(engine) % (max - min));
	return oss.str();
}
std::string random_user() 
{
	static const std::vector<std::string> TABLE =
	{
		"Administrator", "System", "MYLS",
	};
	static std::uniform_int_distribution<uint> GENERATOR(0U, TABLE.size() - 1U);

	return "'" + TABLE[GENERATOR(engine)] + '\'';
}
std::string random_name() 
{
	static const std::vector<std::string> TABLE =
	{
		"dllhost.exe",
		"svchost.exe",
		"chrome.exe",
		"explorer.exe",
		"telegram.exe",
		"vmware.exe",
		"Launchy.exe",
		"clover.exe",
		"sublime_text.exe",
		"cmd.exe",
		"Everything.exe",
		"VsHub.exe",
		"vmtoolsd.exe",
		"sqlservr.exe",
		"foobar2000.exe",
		"CloudMusic.exe",
		"Everything.exe"
	};
	static std::uniform_int_distribution<uint> GENERATOR(0U, TABLE.size() - 1U);
	
	return "'" + TABLE[GENERATOR(engine)] + '\'';
}
std::string random_date() 
{
	static std::uniform_int_distribution<uint> YEAR_GENERATOR(2014U, 2015U);
	static std::uniform_int_distribution<uint> MONTH_GENERATOR(1U, 12U);
	static std::uniform_int_distribution<uint> DAY_GENERATOR(1U, 28U);
	
	std::ostringstream oss;
	oss 
		<< '\''
		<< YEAR_GENERATOR(engine)
		<< std::setw(2) << std::setfill('0') << MONTH_GENERATOR(engine)
		<< std::setw(2) << std::setfill('0') << DAY_GENERATOR(engine)
		<< '\''
		;

	return oss.str();
}

std::string random_records()
{
	return 
		"EXEC InsertRecord "
		+ random_name() + ',' + ' '
		+ random_user() + ',' + ' '
		+ random_date() + ',' + ' '
		+ random_number(0U, 96U) + ',' + ' '
		+ random_number(0U, 100U) + ',' + ' '
		+ random_number(0U, 1024U) + ',' + ' '
		+ random_number(600, 900U) + '\n'
		;
}

int main(int argc, char* argv[])
{
	int count = 100;
	if (argc > 1) {
		std::stringstream ss;
		ss << argv[1];
		ss >> count;
	}

	std::cout 
		<< "-- CODE WAS AUTO-GENERATED BY MYLS\n"
		<< "-- TIME : " << time(0)
		<< std::endl;
	
	for (auto i = 0; i < count; i++)
		std::cout << random_records();

	std::cout
		<< "EXEC InsertLog 2, "
		<< "'" << count << " random records were inserted to database!'" 
		<< std::endl;

	return 0;
}
