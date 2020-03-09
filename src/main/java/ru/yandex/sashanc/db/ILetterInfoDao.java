package ru.yandex.sashanc.db;

import ru.yandex.sashanc.pojo.LetterInfoNot;

import java.nio.file.Path;
import java.util.List;

public interface ILetterInfoDao {
    void createLetter(List<LetterInfoNot> letterInfoNots1, List<LetterInfoNot> letterInfoNots2,  Path path);
}
