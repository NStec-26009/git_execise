// コメントアウトを前文でするやり方 ＝ コントロールA ＋ /

package model.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.dto.Employee;
import model.exception.ServiceException;
import util.TestUtil;

/**
 * UC05【社員削除】機能のテストクラス<br>
 *
 * @author Fullness, Inc.
 *
 */
@DisplayName("UC05【社員削除】機能のテスト")
public class DeleteEmployeeListServiceTest {

    /**
     * テスト対象
     */
    DeleteEmployeeListService target;

    /**
     * 後処理
     * 
     * @throws Exception
     */
    @AfterAll
    public static void tearDownAfterClass() throws Exception {
        TestUtil.initDB();
        TestUtil.setDS101ToDB();
        TestUtil.setDS001ToDB();
    }

    /**
     * 各テスト前に実行
     * 
     * @throws Exception
     */
    @BeforeEach
    public void setUp() throws Exception {
        TestUtil.initDB();
        target = new DeleteEmployeeListService();
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:データあり")
    public void testreadEmployeeByEmpId01() throws Exception {
        TestUtil.setDS101ToDB();
        TestUtil.setDS001ToDB();
        List<Employee> expected = TestUtil.getDS001();
        List<Employee> actual = target.readEmployeeByEmpId();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:データなし")
    public void testreadEmployeeByEmpId02() throws Exception {
        TestUtil.setDS101ToDB();
        TestUtil.setDS002ToDB();
        List<Employee> expected = TestUtil.getDS002();
        List<Employee> actual = target.readEmployeeByEmpId();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:例外処理（取得失敗）")
    public void testreadEmployeeByEmpId03() throws Exception {
        TestUtil.clearDB();
        assertThrows(ServiceException.class, () -> target.readEmployeeByEmpId());
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:例外処理（DB処理エラー）")
    public void testreadEmployeeByEmpId04() throws Exception {
        TestUtil.changeDBSetting();
        try {
            assertThrows(ServiceException.class, () -> target.readEmployeeByEmpId());
        } finally {
            TestUtil.resetDBSetting();
        }
    }
}
