// コメントアウトを前文でするやり方 ＝ コントロールA ＋ /

package model.service;

import static org.junit.jupiter.api.Assertions.*;

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
public class DeleteEmployeeServiceTest {

    /**
     * テスト対象
     */
    DeleteEmployeeService target;

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
        target = new DeleteEmployeeService();
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:データあり")
    public void testReadEmployeeByEmpId01() throws Exception {
        TestUtil.setDS101ToDB();
        TestUtil.setDS001ToDB();
        Employee expected = TestUtil.emp1001;
        Employee actual = target.readEmployeeByEmpId(1001);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:データなし")
    public void testReadEmployeeByEmpId02() throws Exception {
        TestUtil.setDS101ToDB();
        TestUtil.setDS002ToDB();
        assertThrows(ServiceException.class, () -> target.readEmployeeByEmpId(1001));
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:例外処理（取得失敗）")
    public void testReadEmployeeByEmpId03() throws Exception {
        TestUtil.clearDB();
        assertThrows(ServiceException.class, () -> target.readEmployeeByEmpId(1001));
    }

    @Test
    @DisplayName("指定された社員IDに該当する社員情報を取得:例外処理（DB処理エラー）")
    public void testReadEmployeeByEmpId04() throws Exception {
        TestUtil.changeDBSetting();
        try {
            assertThrows(ServiceException.class, () -> target.readEmployeeByEmpId(1001));
        } finally {
            TestUtil.resetDBSetting();
        }
    }
}
